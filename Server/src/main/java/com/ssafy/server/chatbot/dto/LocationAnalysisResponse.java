package com.ssafy.server.chatbot.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationAnalysisResponse {
    private Map<String, Double> categoryScores;  // 카테고리별 점수
    private Double totalScore;  // 종합 점수
    private String analysis;    // GPT 분석 결과
}
