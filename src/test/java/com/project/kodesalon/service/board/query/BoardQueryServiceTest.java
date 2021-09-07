package com.project.kodesalon.service.board.query;

import com.project.kodesalon.domain.board.vo.Content;
import com.project.kodesalon.domain.board.vo.Title;
import com.project.kodesalon.domain.member.vo.Alias;
import com.project.kodesalon.repository.board.BoardRepository;
import com.project.kodesalon.repository.board.query.dto.BoardFlatQueryDto;
import com.project.kodesalon.service.dto.response.MultiBoardSelectResponse;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardQueryServiceTest {

    @InjectMocks
    private BoardQueryService boardQueryService;

    @Mock
    private BoardRepository boardRepository;

    @ParameterizedTest
    @CsvSource(value = {"1, false", "10, true"})
    @DisplayName("마지막 게시물 식별 번호, 조회할 게시물 크기를 전달 받아 복수 게시물을 조회하고 복수 게시물과 다음 게시물이 있는지 여부를 반환한다.")
    void selectBoards(int size, boolean last) {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        BoardFlatQueryDto flats1 = new BoardFlatQueryDto(1L, new Title("게시물 제목"), new Content("게시물 내용"), LocalDateTime.now(), 1L, new Alias("alias1"), 1L, "localhost:8080/bucket/image1.jpg");
        BoardFlatQueryDto flats2 = new BoardFlatQueryDto(1L, new Title("게시물 제목"), new Content("게시물 내용"), LocalDateTime.now(), 1L, new Alias("alias1"), 2L, "localhost:8080/bucket/image2.jpg");
        BoardFlatQueryDto flats3 = new BoardFlatQueryDto(2L, new Title("게시물 제목"), new Content("게시물 내용"), LocalDateTime.now(), 1L, new Alias("alias2"), 3L, "localhost:8080/bucket/image3.jpg");
        BoardFlatQueryDto flats4 = new BoardFlatQueryDto(2L, new Title("게시물 제목"), new Content("게시물 내용"), LocalDateTime.now(), 1L, new Alias("alias2"), null, null);
        List<BoardFlatQueryDto> boardFlatQueryDtos = Arrays.asList(flats1, flats2, flats3, flats4);
        given(boardRepository.selectQueryBoards(anyLong(), anyInt())).willReturn(boardFlatQueryDtos);

        MultiBoardSelectResponse multiBoardSelectResponse = boardQueryService.selectBoards(10L, size);

        softly.then(multiBoardSelectResponse.getBoards()).isNotNull();
        softly.then(multiBoardSelectResponse.isLast()).isEqualTo(last);
        softly.assertAll();
    }
}
