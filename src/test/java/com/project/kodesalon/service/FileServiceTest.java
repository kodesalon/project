package com.project.kodesalon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static org.assertj.core.api.BDDAssertions.then;

class FileServiceTest {
    private FileService fileService;

    @BeforeEach
    void setUp() {
        fileService = new FileService("src/main/resources/images/");
    }

    @Test
    @DisplayName("MultipartFile을 인자로 받아 파일을 생성하여 반환한다")
    void convertFrom() {
        MultipartFile multipartFile = new MockMultipartFile("file", "mock1.png", "image/png", "test data".getBytes());

        File file = fileService.convertFrom(multipartFile);

        then(file).isNotEmpty();
        file.delete();
    }

    @Test
    @DisplayName("파일을 삭제한다.")
    void removeNewFile() {
        MultipartFile multipartFile = new MockMultipartFile("file", "mock1.png", "image/png", "test data".getBytes());
        File file = fileService.convertFrom(multipartFile);

        fileService.removeNewFile(file);

        then(file.exists()).isFalse();
    }
}
