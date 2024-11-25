package com.ssafy.server.chatbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OpenAiService {

    private final WebClient webClient;
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final String GPT_MODEL = "gpt-3.5-turbo";

    public OpenAiService(WebClient.Builder webClientBuilder,
        @Value("${openai.api-key}") String apiKey) {
        this.webClient = webClientBuilder
            .baseUrl(OPENAI_URL)
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .build();
    }

    public String generateAnalysis(String prompt) {
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(message);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", GPT_MODEL);
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);

        try {
            return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                    Map<String, Object> choice = choices.get(0);
                    Map<String, Object> messageResponse = (Map<String, Object>) choice.get("message");
                    return (String) messageResponse.get("content");
                })
                .block();
        } catch (Exception e) {
            log.error("GPT API 호출 중 오류 발생", e);
            throw new RuntimeException("GPT API 호출 중 오류 발생: " + e.getMessage());
        }
    }
}