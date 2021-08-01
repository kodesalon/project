package com.project.kodesalon.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import static com.project.kodesalon.exception.ErrorCode.INVALID_MULTIPART_FILE;

@Slf4j
@Component
public class S3Uploader {
    private static final String DIRECTORY_DELIMITER = "/";

    private final AmazonS3 amazonS3;
    private final String bucket;

    public S3Uploader(final AmazonS3 amazonS3, @Value("${aws.s3.image.bucket}") final String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    public String upload(MultipartFile multipartFile, String directoryName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> {
                    log.info("파일로 변환할 수 없습니다. : {}", multipartFile.getOriginalFilename());
                    throw new IllegalArgumentException(INVALID_MULTIPART_FILE);
                });

        return upload(uploadFile, directoryName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + DIRECTORY_DELIMITER + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

}
