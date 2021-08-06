package com.project.kodesalon.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

@ExtendWith(SpringExtension.class)
public class S3UploaderTest {

    public static final String BUCKET = "testbucket";

    private S3Uploader s3Uploader;
    private S3Mock s3Mock;
    private AmazonS3 client;

    @BeforeEach
    void setUp() {
        s3Mock = new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
        s3Mock.start();

        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration("http://localhost:8001", "ap-northeast-2");
        client = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();

        client.createBucket(BUCKET);
        s3Uploader = new S3Uploader(client, BUCKET);
    }

    @AfterEach
    void tearDown() {
        s3Mock.shutdown();
    }

    @Test
    @DisplayName("S3 bucket에 이미지 파일을 저장하고, 저장한 경로를 반환한다.")
    void upload() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "mock1.png",
                "image/png", "test data".getBytes());

        String result = s3Uploader.upload(mockMultipartFile, "static");

        System.out.println("TEST: " + result);
        then(result).isNotNull();
    }

    @Test
    @DisplayName("S3 bucket에 있는 이미지 파일을 삭제한다.")
    void delete() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "mock1.png",
                "image/png", "test data".getBytes());
        String fileUrl = s3Uploader.upload(mockMultipartFile, "static");
        int indexOfFileName = fileUrl.lastIndexOf("/");
        String fileUrlWithoutFileName = fileUrl.substring(0, indexOfFileName);
        int indexOfDirectoryName = fileUrlWithoutFileName.lastIndexOf("/");
        String key = fileUrl.substring(indexOfDirectoryName + 1);

        s3Uploader.delete(key);

        thenThrownBy(() -> client.getObject(BUCKET, key))
                .isInstanceOf(AmazonS3Exception.class);
    }
}
