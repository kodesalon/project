package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.dto.BoardCreateRequestDto;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import com.project.kodesalon.model.board.exception.ForbiddenException;
import com.project.kodesalon.model.board.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public void save(BoardCreateRequestDto boardCreateRequestDto) {
        Board board = boardEntityFrom(boardCreateRequestDto);
        boardRepository.save(board);
    }

    private Board boardEntityFrom(BoardCreateRequestDto boardCreateRequestDto) {
        try {
            return Board.builder()
                    .title(new Title(boardCreateRequestDto.getTitle()))
                    .content(new Content(boardCreateRequestDto.getContent()))
                    .createdDateTime(LocalDateTime.parse(boardCreateRequestDto.getCreatedDateTime()))
                    .build();
        } catch (Exception e) {
            throw new ForbiddenException(e.getMessage());
        }
    }
}
