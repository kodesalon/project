package com.project.kodesalon.model.board.controller;

import com.project.kodesalon.model.board.service.BoardService;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardUpdateRequest;
import com.project.kodesalon.model.board.service.dto.BoardUpdateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping(value = "/boards")
    public ResponseEntity<Void> save(@RequestBody @Valid final BoardCreateRequest boardCreateRequest) {
        boardService.save(boardCreateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/boards/{boardId}")
    public ResponseEntity<BoardUpdateResponse> updateBoard(@PathVariable final Long boardId, @RequestBody @Valid final BoardUpdateRequest boardUpdateRequest) {
        return ResponseEntity.ok().body(new BoardUpdateResponse("게시물 정보가 변경되었습니다"));
    }
}
