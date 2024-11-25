package com.ssafy.server.chatbot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "에러 응답")
@Getter
@AllArgsConstructor
public class ErrorResponse {
    @Schema(description = "에러 메시지", example = "검색 처리 중 오류가 발생했습니다.")
    private final String message;

    @Schema(description = "상세 에러 내용", example = "External API connection timeout")
    private final String detail;
}
