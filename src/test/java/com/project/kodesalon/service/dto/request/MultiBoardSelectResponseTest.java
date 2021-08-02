package com.project.kodesalon.model.board.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

class MultiBoardSelectResponseTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    @DisplayName("여러 개의 게시물 조회 DTO의 수가 입력 크기보다 같거나 작을 경우, 게시물 DTO를 그대로 반환한다.")
    void getBoards(int size) {
        BoardSelectResponse boardSelectResponse1 = new BoardSelectResponse(1L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias");
        BoardSelectResponse boardSelectResponse2 = new BoardSelectResponse(2L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias");
        MultiBoardSelectResponse multiBoardSelectResponse = new MultiBoardSelectResponse(Arrays.asList(boardSelectResponse1, boardSelectResponse2), size);

        List<BoardSelectResponse> boards = multiBoardSelectResponse.getBoards();

        then(boards.size()).isEqualTo(size);
    }

    @Test
    @DisplayName("여러 개의 게시물 조회 DTO의 수가 입력 크기보다 클 경우, 입력 크기만큼 게시물 DTO를 그대로 반환한다.")
    void getBoards() {
        int size = 2;
        BoardSelectResponse boardSelectResponse1 = new BoardSelectResponse(1L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias");
        BoardSelectResponse boardSelectResponse2 = new BoardSelectResponse(2L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias");
        BoardSelectResponse boardSelectResponse3 = new BoardSelectResponse(3L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias");
        MultiBoardSelectResponse multiBoardSelectResponse = new MultiBoardSelectResponse(Arrays.asList(boardSelectResponse1, boardSelectResponse2, boardSelectResponse3), size);

        List<BoardSelectResponse> boards = multiBoardSelectResponse.getBoards();

        then(boards.size()).isEqualTo(size);
    }

    @ParameterizedTest
    @CsvSource(value = {"1, false", "2, true"})
    @DisplayName("조회해온 게시물이 마지막 게시물일 경우 참을 반환한다.")
    void isLast(int size, boolean expected) {
        BoardSelectResponse boardSelectResponse1 = new BoardSelectResponse(1L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias");
        BoardSelectResponse boardSelectResponse2 = new BoardSelectResponse(2L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias");
        MultiBoardSelectResponse multiBoardSelectResponse = new MultiBoardSelectResponse(Arrays.asList(boardSelectResponse1, boardSelectResponse2), size);

        boolean last = multiBoardSelectResponse.isLast();

        then(last).isEqualTo(expected);
    }
}
