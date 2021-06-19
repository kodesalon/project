package com.project.kodesalon.model.board.controller;

import com.project.kodesalon.model.board.service.BoardService;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardSelectSingleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BoardSelectSingleResponse> selectBoard(@PathVariable final Long boardId) {
        BoardSelectSingleResponse boardSelectSingleResponse = new BoardSelectSingleResponse("제목", "내용", "생성 시간", "작성자 별명");
        return ResponseEntity.ok().body(boardSelectSingleResponse);
    }
}
