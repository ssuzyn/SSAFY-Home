package com.ssafy.server.chatbot.service;

import com.ssafy.server.chatbot.dto.FacilityDetail;
import com.ssafy.server.chatbot.dto.LocationAnalysisRequest;
import com.ssafy.server.chatbot.dto.LocationAnalysisResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationAnalysisService {

    private final OpenAiService openAiService;

    private static final Map<String, Double> CATEGORY_WEIGHTS = Map.of(
        "SW8", 5.0,  // 지하철역
        "SC4", 4.0,  // 학교
        "MT1", 3.0,  // 마트
        "HP8", 3.0,  // 병원
        "BK9", 2.0,  // 은행
        "CE7", 1.0,  // 카페
        "FD6", 1.0   // 음식점
    );

    private static final Map<Integer, Double> DISTANCE_WEIGHTS = Map.of(
        100, 1.0,   // 100m 이내
        300, 0.8,   // 300m 이내
        500, 0.5,   // 500m 이내
        1000, 0.2   // 1km 이내
    );

    public LocationAnalysisResponse analyzeLocation(LocationAnalysisRequest request) {
        Map<String, Double> categoryScores = calculateCategoryScores(request.getFacilityDetails());
        double totalScore = calculateTotalScore(categoryScores);
        String analysis = generateAnalysis(request, categoryScores, totalScore);

        return new LocationAnalysisResponse(categoryScores, totalScore, analysis);
    }

    private Map<String, Double> calculateCategoryScores(Map<String, List<FacilityDetail>> facilityDetails) {
        Map<String, Double> scores = new HashMap<>();

        for (Map.Entry<String, List<FacilityDetail>> entry : facilityDetails.entrySet()) {
            String category = entry.getKey();
            List<FacilityDetail> facilities = entry.getValue();

            double categoryScore = facilities.stream()
                .mapToDouble(facility -> calculateFacilityScore(facility.getDistance(), category))
                .sum();

            scores.put(category, Math.min(categoryScore, 100.0));
        }

        return scores;
    }

    private double calculateFacilityScore(double distance, String category) {
        double distanceWeight = DISTANCE_WEIGHTS.entrySet().stream()
            .filter(entry -> distance <= entry.getKey())
            .findFirst()
            .map(Map.Entry::getValue)
            .orElse(0.0);

        return distanceWeight * CATEGORY_WEIGHTS.getOrDefault(category, 1.0);
    }

    private double calculateTotalScore(Map<String, Double> categoryScores) {
        double weightedSum = categoryScores.entrySet().stream()
            .mapToDouble(entry -> entry.getValue() * CATEGORY_WEIGHTS.getOrDefault(entry.getKey(), 1.0))
            .sum();

        double totalWeight = CATEGORY_WEIGHTS.values().stream().mapToDouble(Double::doubleValue).sum();

        return weightedSum / totalWeight;
    }

    private String generateAnalysis(
        LocationAnalysisRequest request,
        Map<String, Double> categoryScores,
        double totalScore) {

        StringBuilder prompt = new StringBuilder();
        prompt.append("다음 부동산의 입지를 분석해주세요:\n\n");
        prompt.append("주소: ").append(request.getAddress()).append("\n\n");

        prompt.append("카테고리별 평가 점수 (100점 만점):\n");
        for (Map.Entry<String, Double> entry : categoryScores.entrySet()) {
            String categoryName = getCategoryName(entry.getKey());
            prompt.append(String.format("- %s: %.1f점\n", categoryName, entry.getValue()));
        }

        prompt.append(String.format("\n종합 점수: %.1f점\n\n", totalScore));

        prompt.append("위 정보를 바탕으로 다음 항목들을 분석해주세요:\n");
        prompt.append("1. 입지의 주요 장점\n");
        prompt.append("2. 보완이 필요한 점\n");
        prompt.append("3. 투자 관점의 평가\n");
        prompt.append("4. 실거주 관점의 평가\n");
        prompt.append("5. 종합 의견");

        return openAiService.generateAnalysis(prompt.toString());
    }

    private String getCategoryName(String categoryCode) {
        return switch (categoryCode) {
            case "SW8" -> "지하철역";
            case "SC4" -> "학교";
            case "BK9" -> "은행";
            case "MT1" -> "마트";
            case "HP8" -> "병원";
            case "CE7" -> "카페";
            case "FD6" -> "음식점";
            default -> categoryCode;
        };
    }
}
