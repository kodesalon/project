package com.project.kodesalon.model.board.controller;

import com.project.kodesalon.common.annotation.Login;
import com.project.kodesalon.model.board.service.BoardService;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@Login Long memberId, @RequestBody @Valid final BoardCreateRequest boardCreateRequest) {
        boardService.save(memberId, boardCreateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
