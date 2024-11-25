package com.ssafy.server.news.service;

import com.ssafy.server.news.dto.NewsDto;
import com.ssafy.server.news.mapper.NewsMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    // 매일 오전 10시에 실행
    // @Scheduled(cron = "0 0 10 * * *")
    public void fetchAndProcessNews() {
        List<NewsDto> newsList = fetchTodayNews();
        for (NewsDto news : newsList) {
            newsMapper.insertNews(news); // DB에 저장
        }
    }

    private List<NewsDto> fetchTodayNews() {
        String url = String.format("%s?engine=google_news&q=부동산&hl=ko&gl=kr&num=10&api_key=%s", SERPAPI_URL, serpApiKey);
        try {
            String response = restTemplate.getForObject(url, String.class);
            return parseNewsResults(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch news from SerpAPI", e);
        }
    }

    private List<NewsDto> parseNewsResults(String jsonResponse) {
        List<NewsDto> results = new ArrayList<>();
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(jsonResponse);
            JSONArray newsResults = (JSONArray) json.get("news_results");

            for (Object item : newsResults) {
                JSONObject newsItem = (JSONObject) item;
                results.add(NewsDto.builder()
                    .title((String) newsItem.get("title"))
                    .link((String) newsItem.get("link"))
                    .date(LocalDate.now()) // 오늘 날짜 설정
                    .build());
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
