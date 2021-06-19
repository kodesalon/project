package com.project.kodesalon.model.board.controller;

import com.project.kodesalon.model.board.service.BoardService;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardDeleteRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping()
    public ResponseEntity<Void> save(@RequestBody @Valid final BoardCreateRequest boardCreateRequest) {
        boardService.save(boardCreateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestBody final BoardDeleteRequest boardDeleteRequest) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
