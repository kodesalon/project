package com.project.kodesalon.service.image;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.image.Image;
import com.project.kodesalon.repository.image.ImageRepository;
import com.project.kodesalon.service.S3Uploader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;
    private final String directory;

    public ImageService(final ImageRepository imageRepository, final S3Uploader s3Uploader,
                        @Value("${cloud.aws.s3.image.directory}") final String directory) {
        this.imageRepository = imageRepository;
        this.s3Uploader = s3Uploader;
        this.directory = directory;
    }

    public void save(final List<MultipartFile> multipartFiles, final Board board) throws IOException {
        if (multipartFiles == null) {
            return;
        }

        for (MultipartFile multipartFile : multipartFiles) {
            String url = s3Uploader.upload(multipartFile, directory);
            Image image = new Image(url, board);
            imageRepository.save(image);
        }
    }
}
