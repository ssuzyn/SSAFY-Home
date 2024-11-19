package com.ssafy.server.board.service;

import com.ssafy.server.board.dto.BoardDto;
import com.ssafy.server.board.dto.CommentDto;

import java.util.List;

public interface BoardService {

    // Q&A CRUD
    void createBoard(BoardDto boardDto);

    BoardDto getBoard(int articleNo);

    void updateBoard(BoardDto boardDto);

    void deleteBoard(int articleNo);

    // Q&A 조회수 상승
    void incrementHit(int articleNo);

    // 댓글 작성/삭제
    void createComment(CommentDto commentDto);

    void deleteComment(int commentId);

    // Q&A글 별 댓글 조회
    List<CommentDto> getCommentsByArticleNo(int articleNo);

    // Q&A 검색 필터링
    List<BoardDto> searchBoard(String type, String keyword);
}
