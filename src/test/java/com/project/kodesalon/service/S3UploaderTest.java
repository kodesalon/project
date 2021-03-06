package com.project.kodesalon.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.MultiObjectDeleteException;
import com.project.kodesalon.config.S3MockConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

@ExtendWith(SpringExtension.class)
@Import(S3MockConfiguration.class)
class S3UploaderTest {

    public static final String BUCKET = "testbucket";

    @Autowired
    private AmazonS3 amazonS3;

    private S3Uploader s3Uploader;

    @BeforeEach
    void setUp() {
        amazonS3.createBucket("testbucket");
        s3Uploader = new S3Uploader(amazonS3, "testbucket", new FileService("src/main/resources/images/"));
    }

    @Test
    @DisplayName("S3 bucket에 이미지 파일을 저장하고, 저장한 경로를 반환한다.")
    void upload() {
        MultipartFile multipartFile = new MockMultipartFile("file", "mock1.png", "image/png", "test data".getBytes());
        List<MultipartFile> multipartFiles = Collections.singletonList(multipartFile);

        List<String> results = s3Uploader.uploadFiles(multipartFiles, "static");

        then(results).isNotNull();
    }

    @Test
    @DisplayName("s3 bucket에 있는 여러 개의 이미지 파일을 삭제한다.")
    void delete_multiple_images() {
        MultipartFile multipartFile = new MockMultipartFile("file", "mock1.png", "image/png", "test data".getBytes());
        List<MultipartFile> multipartFiles = Arrays.asList(multipartFile, multipartFile, multipartFile);
        List<String> imageUrls = s3Uploader.uploadFiles(multipartFiles, "static");
        List<String> imageKeys = imageUrls.stream()
                .map(this::extractKey)
                .collect(Collectors.toList());

        s3Uploader.delete(imageKeys);

        imageKeys.forEach(imageKey ->
                thenThrownBy(() -> amazonS3.getObject(BUCKET, imageKey))
                        .isInstanceOf(AmazonS3Exception.class));
    }

    @Test
    @DisplayName("존재하지 않은 이미지 Key를 삭제할 경우 예외가 발생한다.")
    void delete_images_throws_exception_with_not_exist_image_keys() {
        String imageKey = "bucket/image.png";
        List<String> keys = Collections.singletonList(imageKey);

        thenThrownBy(() -> s3Uploader.delete(keys)).isInstanceOf(MultiObjectDeleteException.class);
    }

    private String extractKey(String url) {
        int indexOfImageName = url.lastIndexOf("/");
        String urlWithoutImageName = url.substring(0, indexOfImageName);
        int indexOfDirectoryName = urlWithoutImageName.lastIndexOf("/");
        return url.substring(indexOfDirectoryName + 1);
    }
}
