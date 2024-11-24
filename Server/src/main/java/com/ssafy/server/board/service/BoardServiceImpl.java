package com.ssafy.server.board.service;

import com.ssafy.server.board.dto.BoardDto;
import com.ssafy.server.board.dto.CommentDto;
import com.ssafy.server.board.mapper.BoardMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;

    public BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Override
    public List<BoardDto> getAllBoards() {
        return boardMapper.getAllBoards();
    }

    @Override
    public void createBoard(BoardDto boardDto) {
        boardMapper.createBoard(boardDto);
    }

    @Override
    public BoardDto getBoard(int articleNo) {
        boardMapper.incrementHit(articleNo);
        return boardMapper.getBoard(articleNo);
    }

    @Override
    public void updateBoard(BoardDto boardDto) {
        boardMapper.updateBoard(boardDto);
    }

    @Override
    public void deleteBoard(int articleNo) {
        boardMapper.deleteBoard(articleNo);
    }

    @Override
    public void incrementHit(int articleNo) {
        boardMapper.incrementHit(articleNo);
    }

    @Override
    public void createComment(CommentDto commentDto) {
        boardMapper.createComment(commentDto);
    }

    @Override
    public void deleteComment(int commentId) {
        boardMapper.deleteComment(commentId);
    }

    @Override
    public void updateComment(CommentDto commentDto) {
        boardMapper.updateComment(commentDto);
    }

    @Override
    public List<CommentDto> getCommentsByArticleNo(int articleNo) {
        return boardMapper.getCommentsByArticleNo(articleNo);
    }

    @Override
    public List<BoardDto> searchBoard(String type, String keyword) {
        return boardMapper.searchBoard(type, keyword);
    }
}