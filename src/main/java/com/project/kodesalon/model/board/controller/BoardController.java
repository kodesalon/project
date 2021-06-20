package com.project.kodesalon.model.board.controller;

import com.project.kodesalon.model.board.service.BoardService;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardDeleteRequest;
import com.project.kodesalon.model.board.service.dto.BoardSelectMultiResponse;
import com.project.kodesalon.model.board.service.dto.BoardSelectSingleResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping(value = "/api/v1/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid final BoardCreateRequest boardCreateRequest) {
        boardService.save(boardCreateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody @Valid final BoardDeleteRequest boardDeleteRequest) {
        boardService.delete(boardDeleteRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardSelectSingleResponse> selectBoard(@PathVariable final Long boardId) {
        BoardSelectSingleResponse boardSelectSingleResponse = boardService.selectBoard(boardId);
        return ResponseEntity.ok().body(boardSelectSingleResponse);
    }

    @GetMapping
    public ResponseEntity<BoardSelectMultiResponse> selectBoards(@PageableDefault(sort = "board_id", direction = Sort.Direction.DESC) final Pageable pageable) {
        BoardSelectSingleResponse boardSelectSingleResponse = new BoardSelectSingleResponse("제목", "내용", LocalDateTime.now().toString(), "작성자");
        BoardSelectMultiResponse boardSelectMultiResponse = new BoardSelectMultiResponse(Arrays.asList(boardSelectSingleResponse));
        return ResponseEntity.ok().body(boardSelectMultiResponse);
    }
}
