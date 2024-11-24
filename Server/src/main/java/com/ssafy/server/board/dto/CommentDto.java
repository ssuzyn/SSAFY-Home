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
@Schema(title = "CommentDto: Q&A 게시판 댓글 DTO", description = "Q&A 게시판 질문에 대한 댓글 정보를 담는다.")
public class CommentDto {

    @Schema(description = "댓글 고유 ID", example = "1")
    private int commentId;

    @Schema(description = "게시글 번호(QnA 번호)", example = "101")
    private int articleNo;

    @Schema(description = "댓글 작성자 ID", example = "user123")
    private String userId;

    @Schema(description = "댓글 내용", example = "이 질문에 대한 답변입니다.")
    private String content;

    @Schema(description = "사용자 프로필", example = "admin/basic_profile.png")
    private String userProfile;

    @Schema(description = "댓글 등록 시간", example = "2024-11-19T16:05:45")
    private LocalDateTime registerTime;
}
