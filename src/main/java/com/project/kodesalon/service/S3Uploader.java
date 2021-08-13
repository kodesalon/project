package com.project.kodesalon.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.project.kodesalon.exception.ErrorCode.INVALID_IMAGE;

@Slf4j
@Component
public class S3Uploader {

    private static final String DIRECTORY_DELIMITER = "/";
    private static final char EXTENSION_SEPARATOR = '.';

    private final AmazonS3 amazonS3;
    private final String bucket;

    public S3Uploader(final AmazonS3 amazonS3, @Value("${cloud.aws.s3.image.bucket}") final String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    public String upload(final MultipartFile multipartFile, final String directoryName) {
        File file = convert(multipartFile)
                .orElseThrow(() -> {
                    log.info("파일로 변환할 수 없습니다. : {}", multipartFile.getOriginalFilename());
                    throw new IllegalArgumentException(INVALID_IMAGE);
                });

        return upload(file, directoryName);
    }

    private String upload(final File file, final String directoryName) {
        String uuid = UUID.randomUUID().toString();
        String extension = extractExtension(file.getName());
        String fileName = directoryName + DIRECTORY_DELIMITER + uuid + extension;
        String imageUrl = putS3(file, fileName);
        removeNewFile(file);
        return imageUrl;
    }

    private String extractExtension(final String file) {
        int index = file.lastIndexOf(EXTENSION_SEPARATOR);
        return file.substring(index);
    }

    private String putS3(final File file, final String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(final File targetFile) {
        if (targetFile.delete()) {
            log.info("{} 파일이 삭제되었습니다.", targetFile.getName());
            return;
        }

        log.info("{} 파일이 삭제되지 못했습니다.", targetFile.getName());
    }

    private Optional<File> convert(final MultipartFile file) {
        File convertFile = new File(file.getOriginalFilename());

        return createFile(file, convertFile);
    }

    private Optional<File> createFile(final MultipartFile file, final File convertFile) {
        if (canConvertNewFile(convertFile)) {
            createFileOutputStream(file, convertFile);
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private boolean canConvertNewFile(final File convertFile) {
        try {
            return convertFile.createNewFile();
        } catch (IOException e) {
            throw new IllegalArgumentException(INVALID_IMAGE);
        }
    }

    private void createFileOutputStream(final MultipartFile file, final File convertFile) {
        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException(INVALID_IMAGE);
        }
    }

    public void delete(final String key) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
    }
}
