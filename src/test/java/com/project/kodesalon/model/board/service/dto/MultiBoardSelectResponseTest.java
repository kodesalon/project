package com.project.kodesalon.model.board.service.dto;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

class MultiBoardSelectResponseTest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("여러 개의 게시물 조회 DTO의 수가 입력 크기보다 클 경우, 입력 크기만큼 게시물 DTO를 그대로 반환한다.")
    void getBoards(boolean expect) {
        int size = 3;
        BDDSoftAssertions softly = new BDDSoftAssertions();
        BoardSelectResponse boardSelectResponse1 = new BoardSelectResponse(1L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias");
        BoardSelectResponse boardSelectResponse2 = new BoardSelectResponse(2L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias");
        BoardSelectResponse boardSelectResponse3 = new BoardSelectResponse(3L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias");
        MultiBoardSelectResponse multiBoardSelectResponse = new MultiBoardSelectResponse(Arrays.asList(boardSelectResponse1, boardSelectResponse2, boardSelectResponse3), expect);

        List<BoardSelectResponse> boards = multiBoardSelectResponse.getBoards();
        boolean isLast = multiBoardSelectResponse.isLast();

        softly.then(boards.size()).isEqualTo(size);
        softly.then(isLast).isEqualTo(expect);
        softly.assertAll();
    }
}
