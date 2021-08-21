package com.project.kodesalon.controller.image;

import com.project.kodesalon.service.board.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final BoardService boardService;

    public ImageController(final BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestParam final List<MultipartFile> images, @RequestParam final Long boardId) {
        boardService.addImages(boardId, images);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{imageIds}")
    public ResponseEntity<Void> delete(@PathVariable final List<Long> imageIds) {
        boardService.deleteImages(imageIds);
        return ResponseEntity.ok().build();
    }
}
