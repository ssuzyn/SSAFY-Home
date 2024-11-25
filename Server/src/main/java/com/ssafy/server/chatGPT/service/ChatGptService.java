package com.ssafy.server.chatGPT.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    private static final String GPT_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String GPT_MODEL = "gpt-3.5-turbo";

    @Value("${openai.api-key}")
    private String openaiKey;

    private final RestTemplate restTemplate;

    public String getRealEstateAnswer(String question) {
        try {
            String prompt = createPrompt(question);

            // HttpHeaders 생성 및 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + openaiKey);
            headers.set("Content-Type", "application/json");

            // 요청 바디 생성
            Map<String, Object> body = Map.of(
                "model", GPT_MODEL,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "max_tokens", 800
            );

            // HttpEntity 생성
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            // RestTemplate 요청
            String response = restTemplate.postForObject(GPT_API_URL, request, String.class);
            return extractGPTResponse(response);
        } catch (Exception e) {
            return "답변 생성에 실패했습니다: " + e.getMessage();
        }
    }

    private String createPrompt(String question) {
        return String.format("""
            부동산 관련 질문입니다.
            질문: %s
            답변:
            1. 질문에 대한 간략한 설명
            2. 관련 부동산 시장 정보 (있을 경우)
        """, question);
    }

    private String extractGPTResponse(String gptResponse) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(gptResponse);
            JSONArray choices = (JSONArray) json.get("choices");
            JSONObject choice = (JSONObject) choices.get(0);
            JSONObject message = (JSONObject) choice.get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            return "답변 추출에 실패했습니다.";
        }
    }
}

