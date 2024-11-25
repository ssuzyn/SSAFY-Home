package com.ssafy.server.chatbot.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.server.chatbot.dto.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SearchService {
    @Value("${serpapi.api.key}")
    private String serpApiKey;

    @Value("${openai.api.key}")
    private String openAiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SearchService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Cacheable(value = "searchResults", key = "#query", unless = "#result == null")
    public String getSearchAndAnalysis(String query) throws Exception {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("검색어는 필수입니다.");
        }

        try {
            log.debug("Starting search and analysis for query: {}", query);
            // 구글 검색만 수행하도록 수정
            String googleResults = searchGoogle(query);
            return analyzeWithGPT(googleResults, "", query); // 네이버 결과는 빈 문자열로 전달
        } catch (Exception e) {
            log.error("Error in getSearchAndAnalysis for query: {}", query, e);
            throw e;
        }
    }

    public List<SearchResult> getGoogleResults(String query) throws Exception {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("검색어는 필수입니다.");
        }

        try {
            log.debug("Fetching Google results for query: {}", query);
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            String url = UriComponentsBuilder.fromHttpUrl("https://serpapi.com/search")
                    .queryParam("engine", "google")
                    .queryParam("q", encodedQuery)
                    .queryParam("api_key", serpApiKey)
                    .queryParam("gl", "kr")
                    .queryParam("hl", "ko")
                    .build()
                    .toUriString();

            log.debug("Google search URL: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode resultNode = objectMapper.readTree(response.getBody());
                List<SearchResult> results = new ArrayList<>();

                if (resultNode.has("organic_results")) {
                    resultNode.get("organic_results")
                            .elements()
                            .forEachRemaining(result -> {
                                String title = result.has("title") ? result.get("title").asText() : "";
                                String snippet = result.has("snippet") ? result.get("snippet").asText() : "";
                                String link = result.has("link") ? result.get("link").asText() : "";

                                if (!title.isEmpty() && !link.isEmpty()) {
                                    results.add(new SearchResult(title, link, snippet));
                                }
                            });
                }

                log.debug("Found {} Google results for query: {}", results.size(), query);
                return results;
            } else {
                log.error("Failed to get Google results. Status: {}", response.getStatusCode());
                throw new RestClientException("Google search failed with status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Error fetching Google results for query: {} - Error: {}", query, e.getMessage());
            throw e;
        }
    }

    public List<SearchResult> getNaverResults(String query) {
        // 네이버 검색은 임시로 비활성화하고 빈 리스트 반환
        return new ArrayList<>();
    }

    private String searchGoogle(String query) throws Exception {
        List<SearchResult> results = getGoogleResults(query);
        return results.stream()
                .map(result -> result.getTitle() + ": " + result.getSnippet())
                .limit(3)
                .reduce("", (a, b) -> a + "\n" + b);
    }

    private String searchNaver(String query) {
        // 네이버 검색 비활성화
        return "";
    }

    @Retryable(
            value = {ResourceAccessException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    private String analyzeWithGPT(String googleResults, String naverResults, String query) throws Exception {
        log.debug("Analyzing results with GPT for query: {}", query);

        String prompt = String.format("""
            다음은 '%s'에 대한 검색 결과입니다.
            
            구글 검색 결과:
            %s
            
            위 정보를 종합해서 분석하고 요약해주세요. 결과는 다음 형식으로 작성해주세요:
            
            1. 핵심 내용:
            2. 주요 특징:
            3. 추가 정보:
            """, query, googleResults);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiKey);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", "당신은 검색 결과를 분석하고 요약하는 전문가입니다. 결과는 항상 지정된 형식에 맞춰 작성합니다."
                        ),
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                ),
                "temperature", 0.7,
                "max_tokens", 1000
        );

        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions",
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode responseNode = objectMapper.readTree(response.getBody());
                String analysis = responseNode.get("choices").get(0).get("message").get("content").asText();
                log.debug("Successfully analyzed results for query: {}", query);
                return analysis;
            } else {
                log.error("Failed to get GPT analysis. Status: {}", response.getStatusCode());
                throw new RestClientException("GPT analysis failed with status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error in GPT analysis for query: {}", query, e);
            throw e;
        }
    }
}