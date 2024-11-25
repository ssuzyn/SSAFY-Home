package com.ssafy.server.chatGPT.controller;

import com.ssafy.server.chatGPT.service.ChatGptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
@Tag(name = "ChatGPT", description = "사용자의 질문에 대해 GPT 답변을 제공하는 컨트롤러")
public class ChatGptController {

    private final ChatGptService gptService;

    /**
     * 부동산 관련 질문에 대해 GPT 답변을 반환합니다.
     * @param question 사용자가 입력한 질문
     * @return ResponseEntity<Map<String, Object>> - GPT가 생성한 답변 및 상태 코드
     */
    @GetMapping("/ask")
    public ResponseEntity<Map<String, Object>> askGpt(@RequestParam String question) {
        Map<String, Object> response = new HashMap<>();
        try {
            String answer = gptService.getRealEstateAnswer(question);
            response.put("success", true);
            response.put("data", answer);
            return ResponseEntity.ok(response); // 200 OK
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "GPT 서비스 처리 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 500 에러
        }
    }
}
