package com.project.kodesalon.controller.board;

import com.project.kodesalon.config.argumentresolver.annotation.Login;
import com.project.kodesalon.service.board.BoardService;
import com.project.kodesalon.service.dto.request.BoardCreateRequest;
import com.project.kodesalon.service.dto.request.BoardDeleteRequest;
import com.project.kodesalon.service.dto.request.BoardUpdateRequest;
import com.project.kodesalon.service.dto.response.BoardSelectResponse;
import com.project.kodesalon.service.dto.response.MultiBoardSelectResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/boards")
public class BoardController {

    public static final String BOARD_ID_MAX = "9223372036854775807";

    private final BoardService boardService;

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@Login final Long memberId, @RequestBody @Valid final BoardCreateRequest boardCreateRequest,
                                     @RequestParam(required = false) final Optional<List<MultipartFile>> images) {
        List<MultipartFile> boardImages = images.orElseGet(ArrayList::new);
        boardService.save(memberId, boardCreateRequest, boardImages);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> delete(@Login final Long memberId, @PathVariable final Long boardId, @RequestBody @Valid final BoardDeleteRequest boardDeleteRequest) {
        boardService.delete(memberId, boardId, boardDeleteRequest);
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
    public ResponseEntity<MultiBoardSelectResponse> selectBoards(@RequestParam(required = false, defaultValue = BOARD_ID_MAX) final Long lastBoardId, @RequestParam final int size) {
        MultiBoardSelectResponse boardSelectMultiResponse = boardService.selectBoards(lastBoardId, size);
        return ResponseEntity.ok().body(boardSelectMultiResponse);
    }

    @GetMapping("/my-boards")
    public ResponseEntity<MultiBoardSelectResponse> selectMyBoard(@Login final Long memberId, @RequestParam(required = false, defaultValue = BOARD_ID_MAX) final Long lastBoardId, @RequestParam final int size) {
        MultiBoardSelectResponse myBoardSelectResponse = boardService.selectMyBoards(memberId, lastBoardId, size);
        return ResponseEntity.ok().body(myBoardSelectResponse);
    }
}
