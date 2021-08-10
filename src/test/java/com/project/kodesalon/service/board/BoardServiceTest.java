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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_BOARD;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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

    @BeforeEach
    void setUp() {
        boardService
                = new BoardService(boardRepository, memberService, imageRepository, s3Uploader, "static");
    }

    @Test
    @DisplayName("컨트롤러에서 게시판 생성 요청 Dto를 전달받아 게시판을 생성한다.")
    void save() {
        given(memberService.findById(anyLong())).willReturn(member);
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest("게시물 제목", "게시물 작성", LocalDateTime.now());
        MockMultipartFile image = new MockMultipartFile("images", "image.png", "image/png", "test".getBytes());
        List<MultipartFile> images = Arrays.asList(image, image);
        int imageSize = images.size();
        given(s3Uploader.upload(any(MockMultipartFile.class), anyString())).willReturn(IMAGE_UPLOAD_URL);

        boardService.save(anyLong(), boardCreateRequest, images);

        verify(boardRepository, times(1)).save(any(Board.class));
        verify(s3Uploader, times(imageSize)).upload(any(MockMultipartFile.class), anyString());
        verify(imageRepository, times(imageSize)).save(any(Image.class));
    }

    @Test
    @DisplayName("컨트롤러에서 회원 식별 번호, 게시물 식별 번호를 인자로 전달받아 게시물을 삭제한다.")
    void delete() {
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(LocalDateTime.now());
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        boardService.delete(1L, 1L, boardDeleteRequest);

        verify(board, times(1)).delete(anyLong(), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("컨트롤러에서 게시판 수정 요청 Dto를 전달받아 게시판을 수정한다.")
    void update() {
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(member.getId()).willReturn(1L);

        boardService.updateBoard(member.getId(), 1L, BOARD_UPDATE_REQUEST);

        verify(boardRepository, times(1)).findById(anyLong());
        verify(board, times(1)).updateTitleAndContent(anyLong(), any(Title.class), any(Content.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("게시물 수정 요청시 게시물이 존재하지 않으면 예외를 발생시킵니다")
    void update_throws_exception_with_no_board() {
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> boardService.updateBoard(member.getId(), 1L, BOARD_UPDATE_REQUEST))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NOT_EXIST_BOARD);
    }

    @Test
    @DisplayName("컨트롤러에서 게시물 식별 번호를 전달받아 게시물을 조회하고 단일 게시물 조회 응답 DTO를 반환한다.")
    void selectBoard() {
        given(boardRepository.selectBoardById(anyLong())).willReturn(Optional.of(board));
        given(board.getWriter()).willReturn(member);
        given(member.getId()).willReturn(1L);

        BoardSelectResponse boardSelectResponse = boardService.selectBoard(1L);

        then(boardSelectResponse).isNotNull();
        verify(boardRepository).selectBoardById(anyLong());
    }

    @ParameterizedTest
    @CsvSource(value = {"1, false", "10, true"})
    @DisplayName("마지막 게시물 식별 번호를 전달 받아 복수 게시물을 조회하고 복수 게시물과 다음 게시물이 있는지 여부를 반환한다.")
    void selectBoards(int size, boolean last) {
        List<Board> boards = Arrays.asList(board, board);
        given(board.getId()).willReturn(1L);
        given(board.getTitle()).willReturn("게시물 제목");
        given(board.getContent()).willReturn("게시물 내용");
        given(board.getWriter()).willReturn(member);
        given(member.getId()).willReturn(1L);
        given(member.getAlias()).willReturn("alias");
        given(boardRepository.selectBoards(anyLong(), anyInt())).willReturn(boards);

        MultiBoardSelectResponse multiBoardSelectResponse = boardService.selectBoards(10L, size);

        then(multiBoardSelectResponse.getBoards()).isNotNull();
        then(multiBoardSelectResponse.isLast()).isEqualTo(last);
    }
}
