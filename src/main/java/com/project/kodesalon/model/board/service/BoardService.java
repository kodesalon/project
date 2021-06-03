package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public void save(BoardCreateRequestDto boardCreateRequestDto) {
        Board createdBoard = new Board(boardCreateRequestDto.getTitle()
                , boardCreateRequestDto.getContent()
                , "writer"
                , LocalDateTime.parse(boardCreateRequestDto.getCreatedDateTime()));
        boardRepository.save(createdBoard);
    }
}
