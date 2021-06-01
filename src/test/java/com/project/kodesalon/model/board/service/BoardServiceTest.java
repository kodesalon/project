package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.dto.BoardCreateRequestDto;
import com.project.kodesalon.model.board.exception.ForbiddenException;
import com.project.kodesalon.model.board.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.project.kodesalon.model.board.domain.BoardTest.CONTENT;
import static com.project.kodesalon.model.board.domain.BoardTest.CREATED_DATE_TIME;
import static com.project.kodesalon.model.board.domain.BoardTest.TITLE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Test
    @DisplayName("컨트롤러에서 게시판 생성 요청 Dto를 전달받아 게시판을 생성한다.")
    public void save() {
        BoardCreateRequestDto boardCreateRequestDto = new BoardCreateRequestDto(1L, TITLE, CONTENT, CREATED_DATE_TIME.toString());
        boardService.save(boardCreateRequestDto);
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForInvalidDto")
    @DisplayName("Dto를 entity로 변환 시 유효하지 않은 값이 존재할 경우 예외가 발생한다.")
    public void save_fail_invalid_dto(Long memberId, String title, String content, String createdDateTime) {
        BoardCreateRequestDto boardCreateRequestDto = new BoardCreateRequestDto(memberId, title, content, createdDateTime);
        assertThatThrownBy(() -> boardService.save(boardCreateRequestDto))
                .isInstanceOf(ForbiddenException.class);
    }

    private static Stream<Arguments> provideArgumentsForInvalidDto() {
        return Stream.of(
                Arguments.of(1L, "", CONTENT, CREATED_DATE_TIME.toString()),
                Arguments.of(1L, "this title length is 31        ", CONTENT, CREATED_DATE_TIME.toString()),
                Arguments.of(1L, TITLE, "", CREATED_DATE_TIME.toString()),
                Arguments.of(1L, TITLE, "1".repeat(501), CREATED_DATE_TIME.toString())
        );
    }
}
