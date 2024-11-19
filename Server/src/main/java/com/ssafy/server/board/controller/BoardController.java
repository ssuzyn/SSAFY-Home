package com.ssafy.server.board.controller;

import com.ssafy.server.board.dto.BoardDto;
import com.ssafy.server.board.dto.CommentDto;
import com.ssafy.server.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/board")
@Tag(name = "게시판 컨트롤러 ", description = "Q&A 게시판 전반적인 기능 처리")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // Q&A 게시글 작성
    @PostMapping
    @Operation(summary = "게시글 작성", description = "새로운 Q&A 게시글을 작성합니다.")
    public ResponseEntity<Void> createBoard(@RequestBody BoardDto boardDto) {
        boardService.createBoard(boardDto);
        log.info("게시글 작성 완료: {}", boardDto);
        return ResponseEntity.ok().build();
    }

    // Q&A 게시글 조회
    @GetMapping("/{articleNo}")
    @Operation(summary = "게시글 조회", description = "특정 게시글을 조회합니다.")
    public ResponseEntity<BoardDto> getBoard(@PathVariable int articleNo) {
        BoardDto board = boardService.getBoard(articleNo);
        log.info("게시글 조회: {}", board);
        return ResponseEntity.ok(board);
    }

    // Q&A 게시글 수정
    @PutMapping
    @Operation(summary = "게시글 수정", description = "기존 Q&A 게시글을 수정합니다.")
    public ResponseEntity<Void> updateBoard(@RequestBody BoardDto boardDto) {
        boardService.updateBoard(boardDto);
        log.info("게시글 수정 완료: {}", boardDto);
        return ResponseEntity.ok().build();
    }

    // Q&A 게시글 삭제
    @DeleteMapping("/{articleNo}")
    @Operation(summary = "게시글 삭제", description = "특정 Q&A 게시글을 삭제합니다.")
    public ResponseEntity<Void> deleteBoard(@PathVariable int articleNo) {
        boardService.deleteBoard(articleNo);
        log.info("게시글 삭제 완료: {}", articleNo);
        return ResponseEntity.ok().build();
    }

    // 댓글 작성
    @PostMapping("/comments")
    @Operation(summary = "댓글 작성", description = "특정 게시글에 댓글을 작성합니다.")
    public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto) {
        boardService.createComment(commentDto);
        log.info("댓글 작성 완료: {}", commentDto);
        return ResponseEntity.ok().build();
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다.")
    public ResponseEntity<Void> deleteComment(@PathVariable int commentId) {
        boardService.deleteComment(commentId);
        log.info("댓글 삭제 완료: {}", commentId);
        return ResponseEntity.ok().build();
    }

    // 댓글 목록 조회
    @GetMapping("/{articleNo}/comments")
    @Operation(summary = "댓글 조회", description = "특정 게시글에 작성된 댓글 목록을 조회합니다.")
    public ResponseEntity<List<CommentDto>> getCommentsByArticleNo(@PathVariable int articleNo) {
        List<CommentDto> comments = boardService.getCommentsByArticleNo(articleNo);
        log.info("댓글 목록 조회: 게시글 번호 {}, 댓글 개수: {}", articleNo, comments.size());
        return ResponseEntity.ok(comments);
    }

    // Q&A 게시글 검색
    @GetMapping("/search")
    @Operation(summary = "게시글 검색", description = "키워드를 사용하여 게시글을 검색합니다.")
    public ResponseEntity<List<BoardDto>> searchBoard(
            @RequestParam String type,
            @RequestParam String keyword) {
        List<BoardDto> results = boardService.searchBoard(type, keyword);
        log.info("게시글 검색: 키워드 '{}', 결과 개수: {}", keyword, results.size());
        return ResponseEntity.ok(results);
    }
}