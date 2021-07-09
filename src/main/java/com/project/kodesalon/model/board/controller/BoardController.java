package com.project.kodesalon.model.board.controller;

import com.project.kodesalon.common.annotation.Login;
import com.project.kodesalon.model.board.service.BoardService;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardSelectResponse;
import com.project.kodesalon.model.board.service.dto.BoardUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@Login final Long memberId, @RequestBody @Valid final BoardCreateRequest boardCreateRequest) {
        boardService.save(memberId, boardCreateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> delete(@Login final Long memberId, @PathVariable final Long boardId) {
        boardService.delete(memberId, boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<Void> updateBoard(@Login final Long memberId, @PathVariable final Long boardId, @RequestBody @Valid final BoardUpdateRequest boardUpdateRequest) {
        boardService.updateBoard(memberId, boardId, boardUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardSelectResponse> selectBoard(@PathVariable final Long boardId) {
        BoardSelectResponse boardSelectResponse = boardService.selectBoard(boardId);
        return ResponseEntity.ok().body(boardSelectResponse);
    }

    @GetMapping
    public ResponseEntity<Page<BoardSelectResponse>> selectBoards(@PageableDefault(sort = "board_id", direction = Sort.Direction.DESC) final Pageable pageable) {
        Page<BoardSelectResponse> boardSelectMultiResponse = boardService.selectBoards(pageable);
        return ResponseEntity.ok().body(boardSelectMultiResponse);
    }
}
