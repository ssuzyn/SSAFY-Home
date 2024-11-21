package com.ssafy.server.board.mapper;

import com.ssafy.server.board.dto.BoardDto;
import com.ssafy.server.board.dto.CommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {

    // 전체 게시글 목록 조회
    List<BoardDto> getAllBoards();

    // Q&A CRUD
    int createBoard(BoardDto boardDto);

    BoardDto getBoard(@Param("articleNo") int articleNo);

    int updateBoard(BoardDto boardDto);

    int deleteBoard(@Param("articleNo") int articleNo);

    // Q&A 조회수 상승
    int incrementHit(@Param("articleNo") int articleNo);

    // 댓글 작성/삭제
    int createComment(CommentDto commentDto);

    int deleteComment(@Param("commentId") int commentId);

    // Q&A글 별 댓글 조회
    List<CommentDto> getCommentsByArticleNo(@Param("articleNo") int articleNo);

    // Q&A 검색 필터링
    List<BoardDto> searchBoard(@Param("type") String type, @Param("keyword") String keyword);
}