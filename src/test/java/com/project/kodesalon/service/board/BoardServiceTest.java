package com.project.kodesalon.service.board;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.board.vo.Content;
import com.project.kodesalon.domain.board.vo.Title;
import com.project.kodesalon.domain.image.Image;
import com.project.kodesalon.domain.member.Member;
import com.project.kodesalon.repository.board.BoardRepository;
import com.project.kodesalon.repository.image.ImageRepository;
import com.project.kodesalon.service.S3Uploader;
import com.project.kodesalon.service.dto.request.BoardCreateRequest;
import com.project.kodesalon.service.dto.request.BoardDeleteRequest;
import com.project.kodesalon.service.dto.request.BoardUpdateRequest;
import com.project.kodesalon.service.dto.response.BoardSelectResponse;
import com.project.kodesalon.service.dto.response.MultiBoardSelectResponse;
import com.project.kodesalon.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_IMAGES_SIZE;
import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_BOARD;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    private static final String IMAGE_UPLOAD_URL = "localhost:8080/bucket/directory/image.jpeg";
    private final BoardUpdateRequest BOARD_UPDATE_REQUEST = new BoardUpdateRequest("update title", "update content", LocalDateTime.now());

    private BoardService boardService;

    @Mock
    private MemberService memberService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private S3Uploader s3Uploader;

    @Mock
    private Member member;

    @Mock
    private Board board;

    @Mock
    MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        boardService = new BoardService(boardRepository, memberService, imageRepository, s3Uploader, "images");
    }

    @Test
    @DisplayName("?????????????????? ????????? ?????? ?????? Dto??? ???????????? ???????????? ????????????.")
    void save() {
        given(memberService.findById(anyLong())).willReturn(member);
        MockMultipartFile image = new MockMultipartFile("images", "image.png", "image/png", "test".getBytes());
        List<MultipartFile> images = Arrays.asList(image, image);
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest("????????? ??????", "????????? ??????", LocalDateTime.now(), Optional.of(images));

        boardService.save(anyLong(), boardCreateRequest);

        verify(boardRepository, times(1)).save(any(Board.class));
        verify(s3Uploader, times(1)).uploadFiles(anyList(), anyString());
    }

    @Test
    @DisplayName("?????????????????? ?????? ?????? ??????, ????????? ?????? ????????? ????????? ???????????? ???????????? ????????????.")
    void delete() {
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(LocalDateTime.now());
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        boardService.delete(1L, 1L, boardDeleteRequest);

        verify(board, times(1)).delete(anyLong(), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("????????? ???????????? ???????????? ???????????? ????????? ???????????? ????????????.")
    void addImage() {
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        MockMultipartFile image = new MockMultipartFile("images", "image.png", "image/png", "test".getBytes());
        List<MultipartFile> images = Arrays.asList(image, image);
        boardService.addImages(1L, images);
        verify(s3Uploader, times(1)).uploadFiles(anyList(), anyString());
    }

    @Test
    @DisplayName("?????????????????? ????????? ?????? ?????? Dto??? ???????????? ???????????? ????????????.")
    void update() {
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(member.getId()).willReturn(1L);

        boardService.updateBoard(member.getId(), 1L, BOARD_UPDATE_REQUEST);

        verify(boardRepository, times(1)).findById(anyLong());
        verify(board, times(1)).updateTitleAndContent(anyLong(), any(Title.class), any(Content.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("???????????? ???????????? ???????????? ????????????.")
    void addImages() {
        List<MultipartFile> multipartFiles = Arrays.asList(multipartFile, multipartFile);
        given(s3Uploader.uploadFiles(anyList(), anyString())).willReturn(Arrays.asList(IMAGE_UPLOAD_URL, IMAGE_UPLOAD_URL));
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        boardService.addImages(1L, multipartFiles);

        verify(s3Uploader, times(1)).uploadFiles(anyList(), anyString());
    }

    @Test
    @DisplayName("???????????? ????????? 6??? ???????????? ????????? ???????????????")
    void add_throws_exception_with_invalid_board_images_size() {
        List<MultipartFile> multipartFiles
                = Arrays.asList(multipartFile, multipartFile, multipartFile, multipartFile, multipartFile, multipartFile);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(s3Uploader.uploadFiles(anyList(), anyString())).willReturn(Arrays.asList(IMAGE_UPLOAD_URL, IMAGE_UPLOAD_URL,
                IMAGE_UPLOAD_URL, IMAGE_UPLOAD_URL, IMAGE_UPLOAD_URL, IMAGE_UPLOAD_URL));
        willThrow(new IllegalArgumentException(INVALID_BOARD_IMAGES_SIZE)).given(board).addImage(any(Image.class));
        thenIllegalArgumentException().isThrownBy(() -> boardService.addImages(1L, multipartFiles))
                .withMessage(INVALID_BOARD_IMAGES_SIZE);
    }

    @Test
    @DisplayName("???????????? ?????? ??????, ??????????????? ????????? ??????????????? ?????? ?????? ???????????? ????????????.")
    void deleteImages() {
        Board board = mock(Board.class);
        List<Long> imageIds = Arrays.asList(1L, 2L);
        given(board.getId()).willReturn(1L);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        boardService.deleteImages(board.getId(), imageIds);

        verify(boardRepository, times(1)).findById(anyLong());
        verify(imageRepository, times(1)).deleteInBatch(anyList());
        verify(s3Uploader, times(1)).delete(anyList());
    }

    @Test
    @DisplayName("????????? ?????? ????????? ???????????? ???????????? ????????? ????????? ??????????????????")
    void update_throws_exception_with_no_board() {
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> boardService.updateBoard(member.getId(), 1L, BOARD_UPDATE_REQUEST))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NOT_EXIST_BOARD);
    }

    @Test
    @DisplayName("?????????????????? ????????? ?????? ????????? ???????????? ???????????? ???????????? ?????? ????????? ?????? ?????? DTO??? ????????????.")
    void selectBoard() {
        given(boardRepository.selectBoard(anyLong())).willReturn(Optional.of(board));
        given(board.getWriter()).willReturn(member);
        given(board.getCreatedDateTime()).willReturn(LocalDateTime.now());
        given(member.getId()).willReturn(1L);

        BoardSelectResponse boardSelectResponse = boardService.selectBoard(1L);

        then(boardSelectResponse).isNotNull();
        verify(boardRepository, times(1)).selectBoard(anyLong());
    }

    @Test
    @DisplayName("?????????????????? ????????? ?????? ????????? ???????????? ????????? ?????? ??? ???????????? ???????????? ?????? ?????? ????????? ???????????????")
    void selectBoard_throw_exception_with_not_exist_board_id() {
        given(boardRepository.selectBoard(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> boardService.selectBoard(1L)).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(NOT_EXIST_BOARD);
    }

    @Test
    @DisplayName("????????? ????????? ?????? ????????? ????????? ???????????? ????????? ???????????? ????????????.")
    void selectBoards() {
        Board board = mock(Board.class);
        List<Board> boards = List.of(board);
        given(board.getId()).willReturn(1L);
        given(board.getTitle()).willReturn("????????? ??????");
        given(board.getContent()).willReturn("????????? ??????");
        given(board.getCreatedDateTime()).willReturn(LocalDateTime.of(2021, 2, 17, 1, 2, 3));

        Member writer = mock(Member.class);
        given(board.getWriter()).willReturn(writer);
        given(writer.getId()).willReturn(1L);
        given(writer.getAlias()).willReturn("alias");

        Image image = mock(Image.class);
        List<Image> images = List.of(image);
        given(board.getImages()).willReturn(images);
        given(image.getId()).willReturn(1L);
        given(image.getUrl()).willReturn("localhost:8080/image.jpg");

        given(boardRepository.selectBoards(anyLong(), anyLong())).willReturn(boards);

        MultiBoardSelectResponse<BoardSelectResponse> multiBoardSelectResponse = boardService.selectBoards(3L, 10);

        verify(boardRepository, times(1)).selectBoards(anyLong(), anyLong());
        then(multiBoardSelectResponse).isNotNull();
    }
}
