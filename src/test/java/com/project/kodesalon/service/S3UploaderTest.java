package com.project.kodesalon.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.project.kodesalon.config.S3MockConfiguration;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

@ExtendWith(SpringExtension.class)
@Import(S3MockConfiguration.class)
class S3UploaderTest {

    public static final String BUCKET = "testbucket";

    @Autowired
    private S3Mock s3Mock;

    @Autowired
    private AmazonS3 amazonS3;

    private S3Uploader s3Uploader;

    @BeforeEach
    void setUp() {
        s3Uploader = new S3Uploader(amazonS3, "testbucket");
    }

    @Test
    @DisplayName("S3 bucket에 이미지 파일을 저장하고, 저장한 경로를 반환한다.")
    void upload() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "mock1.png", "image/png", "test data".getBytes());

        String result = s3Uploader.upload(mockMultipartFile, "static");

        then(result).isNotNull();
    }

    @Test
    @DisplayName("S3 bucket에 있는 이미지 파일을 삭제한다.")
    void delete() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "mock1.png", "image/png", "test data".getBytes());
        String fileUrl = s3Uploader.upload(mockMultipartFile, "static");
        int indexOfFileName = fileUrl.lastIndexOf("/");
        String fileUrlWithoutFileName = fileUrl.substring(0, indexOfFileName);
        int indexOfDirectoryName = fileUrlWithoutFileName.lastIndexOf("/");
        String key = fileUrl.substring(indexOfDirectoryName + 1);

        s3Uploader.delete(key);

        thenThrownBy(() -> amazonS3.getObject(BUCKET, key))
                .isInstanceOf(AmazonS3Exception.class);
    }
}
