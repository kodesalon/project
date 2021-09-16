package com.project.kodesalon.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import static com.project.kodesalon.exception.ErrorCode.CLOUD_ERROR;

@Slf4j
@Component
public class S3Uploader {

    private static final String DIRECTORY_DELIMITER = "/";

    private final AmazonS3 amazonS3;
    private final String bucket;
    private final FileService fileService;

    public S3Uploader(final AmazonS3 amazonS3, @Value("${cloud.aws.s3.image.bucket}") final String bucket,
                      final FileService fileService) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
        this.fileService = fileService;
    }

    public List<String> uploadFiles(final List<MultipartFile> multipartFiles, final String directoryName) {
        return multipartFiles.stream()
                .map(fileService::convertFrom)
                .map(file -> upload(file, directoryName))
                .collect(Collectors.toList());
    }

    private String upload(final File file, final String directoryName) {
        String fileName = directoryName + DIRECTORY_DELIMITER + file.getName();
        return getS3Url(file, fileName);
    }

    private String getS3Url(final File file, final String fileName) {
        putS3(file, fileName);
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void putS3(final File file, final String fileName) {
        try {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonClientException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(CLOUD_ERROR);
        } finally {
            fileService.removeNewFile(file);
        }
    }

    @Async
    public void delete(final List<String> keys) {
        List<KeyVersion> keyVersions = convertKeyVersionsFrom(keys);
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket);
        deleteObjectsRequest.setKeys(keyVersions);
        amazonS3.deleteObjects(deleteObjectsRequest);
    }

    private List<KeyVersion> convertKeyVersionsFrom(final List<String> keys) {
        return keys.stream()
                .map(KeyVersion::new)
                .collect(Collectors.toList());
    }
}
