package com.project.kodesalon.service.image;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.image.Image;
import com.project.kodesalon.repository.image.ImageRepository;
import com.project.kodesalon.service.S3Uploader;
import com.project.kodesalon.service.board.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
    private static final String IMAGE_UPLOAD_URL = "localhost:8080/bucket/directory/image.jpeg";

    private ImageService imageService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private BoardService boardService;

    @Mock
    private S3Uploader s3Uploader;

    @Mock
    private Board board;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private Image image;

    @BeforeEach
    void setUp() {
        imageService = new ImageService(imageRepository, s3Uploader, "directory");
    }

    @Test
    @DisplayName("이미지와 게시물 식별 번호를 전달받아 이미지를 추가한다.")
    void add() {
        List<MultipartFile> multipartFiles = Arrays.asList(multipartFile, multipartFile);
        given(s3Uploader.upload(any(MultipartFile.class), anyString())).willReturn(IMAGE_UPLOAD_URL);
        int imageSize = multipartFiles.size();

        imageService.add(multipartFiles);

        verify(s3Uploader, times(imageSize)).upload(any(MultipartFile.class), anyString());
    }

    @Test
    @DisplayName("이미지와 게시물 식별 번호를 전달받아 이미지를 추가한다.")
    void add2() {
        List<MultipartFile> multipartFiles = Arrays.asList(multipartFile, multipartFile);
        given(s3Uploader.upload(any(MultipartFile.class), anyString())).willReturn(IMAGE_UPLOAD_URL);
        int imageSize = multipartFiles.size();

        imageService.add(multipartFiles);

        verify(s3Uploader, times(imageSize)).upload(any(MultipartFile.class), anyString());
    }

    @Test
    @DisplayName("삭제하려는 게시물의 식별 번호를 입력 받아 이미지를 삭제한다.")
    void delete() {
        List<Long> imageIds = Arrays.asList(1L, 2L);
        int imageSize = imageIds.size();
        given(imageRepository.findById(anyLong())).willReturn(Optional.of(image));
        given(image.getKey()).willReturn("/static/imageUrl.png");

        imageService.delete(imageIds);

        verify(imageRepository, times(imageSize)).delete(image);
        verify(s3Uploader, times(imageSize)).delete(anyString());
    }
}
