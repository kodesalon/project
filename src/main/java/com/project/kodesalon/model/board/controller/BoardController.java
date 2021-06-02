package com.project.kodesalon.model.board.controller;

import com.project.kodesalon.model.board.controller.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.BoardService;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping(value = "/boards")
    public ResponseEntity<HttpStatus> save(@RequestBody final BoardCreateRequest boardCreateRequest) {
        BoardCreateRequestDto boardCreateRequestDto = new BoardCreateRequestDto(boardCreateRequest.getMemberId(), boardCreateRequest.getTitle(), boardCreateRequest.getContent(), boardCreateRequest.getCreatedDateTime());
        boardService.save(boardCreateRequestDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
