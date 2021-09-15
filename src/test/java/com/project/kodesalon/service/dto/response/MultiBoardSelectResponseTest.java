package com.project.kodesalon.service.dto.response;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class MultiBoardSelectResponseTest {

    @ParameterizedTest
    @CsvSource(value = {"2, false", "3, true"})
    @DisplayName("조회된 게시물 DTO의 수가 조회되야할 게시물 수보다 클 경우, 조회되야할 게시물 수만큼 게시물 DTO를 반환한다.")
    void getBoards(int size, boolean expect) {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        BoardSelectResponse boardSelectResponse1 = new BoardSelectResponse(1L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias", Collections.emptyList());
        BoardSelectResponse boardSelectResponse2 = new BoardSelectResponse(2L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias", Collections.emptyList());
        BoardSelectResponse boardSelectResponse3 = new BoardSelectResponse(3L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias", Collections.emptyList());
        MultiBoardSelectResponse multiBoardSelectResponse = new MultiBoardSelectResponse(Arrays.asList(boardSelectResponse1, boardSelectResponse2, boardSelectResponse3), size);

        List<BoardSelectResponse> boards = multiBoardSelectResponse.getBoards();
        boolean isLast = multiBoardSelectResponse.isLast();

        softly.then(boards.size()).isEqualTo(size);
        softly.then(isLast).isEqualTo(expect);
        softly.assertAll();
    }
}
