package com.project.kodesalon.service.image;

import com.project.kodesalon.domain.image.Image;
import com.project.kodesalon.repository.image.ImageRepository;
import com.project.kodesalon.service.S3Uploader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_IMAGE;

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

    @Transactional
    public List<String> add(final List<MultipartFile> multipartFiles) {
        return multipartFiles.stream()
                .map(multipartFile -> s3Uploader.upload(multipartFile, directory))
                .collect(Collectors.toList());
    }

    @Transactional
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
