package com.ssafy.server.chatbot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "통합 검색 응답 DTO")
@Getter
@AllArgsConstructor
public class SearchResponse {
    @Schema(description = "검색어", example = "Spring Boot")
    private final String query;

    @Schema(description = "GPT 분석 결과", example = "Spring Boot는 자바 기반의 웹 애플리케이션 프레임워크입니다...")
    private final String analysis;

    @Schema(description = "Google 검색 결과 목록")
    private final List<SearchResult> googleResults;

    @Schema(description = "Naver 검색 결과 목록")
    private final List<SearchResult> naverResults;

    @Schema(description = "검색 수행 시간", example = "2024-03-25T10:30:00")
    private final LocalDateTime searchTime;
}