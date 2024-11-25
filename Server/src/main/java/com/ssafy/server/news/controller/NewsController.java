package com.ssafy.server.news.controller;

import com.ssafy.server.news.dto.NewsDto;
import com.ssafy.server.news.service.NewsCrawlingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "부동산 관련 뉴스를 가져와 GPT 분석 결과를 제공하는 컨트롤러")
public class NewsController {

    private final NewsCrawlingService newsService;

    /**
     * 오늘의 부동산 뉴스 가져오기 및 GPT 분석
     * @return ResponseEntity<Map<String, Object>> - 처리 결과와 상태 코드
     */
    @GetMapping("/fetch-today")
    public ResponseEntity<Map<String, Object>> fetchTodayNews() {
        Map<String, Object> response = new HashMap<>();
        try {
            newsService.fetchAndProcessNews();
            response.put("success", true);
            response.put("message", "오늘의 뉴스가 성공적으로 처리되었습니다.");
            return ResponseEntity.ok(response); // 200 OK
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "뉴스 처리 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 500 에러
        }
    }

    /**
     * 데이터베이스에서 모든 뉴스 조회
     * @return ResponseEntity<Map<String, Object>> - 뉴스 목록과 상태 코드
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllNews() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<NewsDto> newsList = newsService.getAllNews();
            response.put("success", true);
            response.put("data", newsList);
            return ResponseEntity.ok(response); // 200 OK
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "뉴스 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 500 에러
        }
    }
}
