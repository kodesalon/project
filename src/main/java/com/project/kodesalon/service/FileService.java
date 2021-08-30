package com.project.kodesalon.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static com.project.kodesalon.exception.ErrorCode.INVALID_FILE;
import static com.project.kodesalon.exception.ErrorCode.INVALID_IMAGE;

@Slf4j
@Component
public class FileService {

    private static final char EXTENSION_SEPARATOR = '.';

    private final String imageResourceDirectory;

    public FileService(@Value("spring.file.directory") final String imageResourceDirectory) {
        this.imageResourceDirectory = imageResourceDirectory;
    }

    public File convertFrom(final MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String extension = extractExtension(file.getOriginalFilename());
        String fileName = imageResourceDirectory + uuid + extension;
        File convertFile = new File(fileName);
        return createFile(file, convertFile);
    }

    private String extractExtension(final String file) {
        int index = file.lastIndexOf(EXTENSION_SEPARATOR);
        return file.substring(index);
    }

    private File createFile(final MultipartFile file, final File convertFile) {
        if (canConvertNewFile(convertFile)) {
            createFileOutputStream(file, convertFile);
            return convertFile;
        }

        log.info("파일 이름 또는 저장 경로를 찾을 수 없습니다: {}", file.getOriginalFilename());
        throw new IllegalArgumentException(INVALID_FILE);
    }

    private boolean canConvertNewFile(final File convertFile) {
        try {
            return convertFile.createNewFile();
        } catch (IOException e) {
            log.info("파일을 변환할 수 없습니다 {} :", convertFile.getName());
            throw new IllegalArgumentException(INVALID_FILE);
        }
    }

    private void createFileOutputStream(final MultipartFile file, final File convertFile) {
        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            removeNewFile(convertFile);
            throw new IllegalArgumentException(INVALID_IMAGE);
        }
    }

    public void removeNewFile(final File targetFile) {
        if (targetFile.delete()) {
            log.info("{} 파일이 삭제되었습니다.", targetFile.getName());
            return;
        }

        log.info("{} 파일이 삭제되지 못했습니다.", targetFile.getName());
    }
}
