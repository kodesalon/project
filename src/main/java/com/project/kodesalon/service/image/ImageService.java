package com.project.kodesalon.service.image;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.image.Image;
import com.project.kodesalon.repository.image.ImageRepository;
import com.project.kodesalon.service.S3Uploader;
import com.project.kodesalon.service.board.BoardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_IMAGE;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final BoardService boardService;
    private final S3Uploader s3Uploader;
    private final String directory;

    public ImageService(final ImageRepository imageRepository, final S3Uploader s3Uploader,
                        final BoardService boardService, @Value("${cloud.aws.s3.image.directory}") final String directory) {
        this.imageRepository = imageRepository;
        this.s3Uploader = s3Uploader;
        this.boardService = boardService;
        this.directory = directory;
    }

    public void add(final List<MultipartFile> multipartFiles, final Long boardId) throws IOException {
        Board board = boardService.findById(boardId);
        uploadImages(multipartFiles, board);
    }

    private void uploadImages(final List<MultipartFile> multipartFiles, final Board board) throws IOException {
        for (MultipartFile multipartFile : multipartFiles) {
            String url = s3Uploader.upload(multipartFile, directory);
            Image image = new Image(url, board);
            imageRepository.save(image);
        }
    }

    public void delete(final List<Long> imageIds) {
        for (Long imageId : imageIds) {
            Image image = findById(imageId);
            String key = image.getKey();
            s3Uploader.delete(key);
            imageRepository.delete(image);
        }
    }

    private Image findById(final Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_IMAGE));
    }
}
