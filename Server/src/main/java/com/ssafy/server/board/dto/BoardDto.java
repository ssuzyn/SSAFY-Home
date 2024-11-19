package com.ssafy.server.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "BoardDto: Q&A 게시판 DTO", description = "Q&A 게시판에 들어갈 내용들을 담는다")
public class BoardDto {
    @Schema(description = "게시글 번호", example = "1")
    private int articleNo;

    @Schema(description = "작성자 ID", example = "user123")
    private String userId;

    @Schema(description = "게시글 제목", example = "Q&A 제목입니다.")
    private String subject;

    @Schema(description = "게시글 내용", example = "게시글 내용입니다.")
    private String content;

    @Schema(description = "조회수", example = "123")
    private int hit;

    @Schema(description = "등록 시간", example = "2024-11-19T16:05:45")
    private LocalDateTime registerTime;
}
