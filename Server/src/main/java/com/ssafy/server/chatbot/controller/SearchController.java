package com.ssafy.server.chatbot.controller;

import com.ssafy.server.chatbot.dto.ErrorResponse;
import com.ssafy.server.chatbot.dto.SearchResponse;
import com.ssafy.server.chatbot.dto.SearchResult;
import com.ssafy.server.chatbot.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/search")
@Tag(name = "Search API", description = "통합 검색 API - Google, Naver 검색 결과와 GPT 분석을 제공합니다")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @Operation(
            summary = "통합 검색 수행",
            description = "입력된 검색어에 대해 Google, Naver 검색 결과와 GPT 분석을 제공합니다"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "검색 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SearchResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<?> search(
            @Parameter(description = "검색어", required = true, example = "Spring Boot")
            @RequestParam String query
    ) {
        try {
            log.info("Searching for query: {}", query);

            String analysis = searchService.getSearchAndAnalysis(query);
            List<SearchResult> googleResults = searchService.getGoogleResults(query);
            List<SearchResult> naverResults = searchService.getNaverResults(query);

            SearchResponse response = new SearchResponse(
                    query,
                    analysis,
                    googleResults,
                    naverResults,
                    LocalDateTime.now()
            );

            log.info("Search completed successfully for query: {}", query);
            return ResponseEntity.ok(response);

        } catch (RestClientException e) {
            log.error("External API error for query: {}", query, e);
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponse("외부 API 호출 중 오류가 발생했습니다.", e.getMessage()));

        } catch (Exception e) {
            log.error("Unexpected error occurred for query: {}", query, e);
            return ResponseEntity
                    .internalServerError()
                    .body(new ErrorResponse("검색 처리 중 오류가 발생했습니다.", e.getMessage()));
        }
    }
}