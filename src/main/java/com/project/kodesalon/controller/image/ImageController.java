package com.project.kodesalon.controller.image;

import com.project.kodesalon.service.image.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(final ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/{boardId}")
    public ResponseEntity<Void> add(@RequestParam final List<MultipartFile> images, @PathVariable final Long boardId) throws IOException {
        imageService.add(images, boardId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{imageIds}")
    public ResponseEntity<Void> delete(@PathVariable final List<Long> imageIds) {
        imageService.delete(imageIds);
        return ResponseEntity.ok().build();
    }
}
