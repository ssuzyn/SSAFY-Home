package com.ssafy.server.news.service;

import com.ssafy.server.news.dto.NewsDto;
import com.ssafy.server.news.mapper.NewsMapper;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsCrawlingService {

    private static final String SERPAPI_URL = "https://serpapi.com/search.json";

    @Value("${serp.api-key}")
    private String serpApiKey;


    private final RestTemplate restTemplate;
    private final NewsMapper newsMapper;

    @Scheduled(cron = "0 0 7,14,20 * * *")  // 매일 오전 7시, 오후 2시, 저녁 8시
    public void fetchAndProcessNews() {
        List<NewsDto> realEstateNews = fetchTodayNews("분양", "sale");
        List<NewsDto> apartmentNews = fetchTodayNews("아파트", "apartment");
        List<NewsDto> reconstructionNews = fetchTodayNews("재건축", "reconstruction");


        // 각 주제별 뉴스 DB 저장
        saveNews(realEstateNews);
        saveNews(apartmentNews);
        saveNews(reconstructionNews);
    }

    private void saveNews(List<NewsDto> newsList) {
        for (NewsDto news : newsList) {
            // URL로 중복 체크
            if (newsMapper.checkDuplicateNewsByUrl(news.getLink()) == 0) {
                newsMapper.insertNews(news);
            } else {
                System.out.println("Duplicate news found, skipping: " + news.getTitle());
            }

        }
    }

    private List<NewsDto> fetchTodayNews(String keyword, String category) {
        String url = String.format("%s?engine=google_news&q=%s&hl=ko&gl=kr&num=50&tbm=nws&api_key=%s", SERPAPI_URL, keyword, serpApiKey);
        System.out.println("SerpAPI 호출 URL: " + url);

        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("API 응답 내용: " + response); // 응답 로그 출력
            return parseNewsResults(response, category); // category 전달
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP 상태 코드: " + e.getStatusCode());
            System.err.println("응답 내용: " + e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch news from SerpAPI", e);
        } catch (Exception e) {
            System.err.println("예외 발생: " + e.getMessage());
            throw new RuntimeException("Failed to fetch news from SerpAPI", e);
        }
    }



    private List<NewsDto> parseNewsResults(String jsonResponse, String category) {
        List<NewsDto> results = new ArrayList<>();
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(jsonResponse);
            JSONArray newsResults = (JSONArray) json.get("news_results");

            for (Object item : newsResults) {
                JSONObject newsItem = (JSONObject) item;

                // stories가 있는 경우 처리
                JSONArray stories = (JSONArray) newsItem.get("stories");
                if (stories != null) {
                    for (Object story : stories) {
                        JSONObject storyItem = (JSONObject) story;
                        String dateString = (String) storyItem.get("date");
                        LocalDate articleDate = LocalDate.now();

                        if (dateString != null) {
                            String datePart = dateString.split(",")[0];
                            articleDate = LocalDate.parse(datePart, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                        }

                        results.add(NewsDto.builder()
                                .title((String) storyItem.get("title"))
                                .link((String) storyItem.get("link"))
                                .date(articleDate)
                                .thumbnail((String) storyItem.get("thumbnail"))
                                .category(category) // 카테고리 설정
                                .build());
                    }
                }
                // stories가 없는 경우 처리
                else if (newsItem.get("title") != null) {
                    String dateString = (String) newsItem.get("date");
                    LocalDate articleDate = LocalDate.now();

                    if (dateString != null) {
                        String datePart = dateString.split(",")[0];
                        articleDate = LocalDate.parse(datePart, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    }

                    results.add(NewsDto.builder()
                            .title((String) newsItem.get("title"))
                            .link((String) newsItem.get("link"))
                            .date(articleDate)
                            .thumbnail((String) newsItem.get("thumbnail"))
                            .category(category) // 카테고리 설정
                            .build());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse news results", e);
        }
        return results;
    }


    public List<NewsDto> getAllNews() {
        return newsMapper.selectNews();
    }

}
