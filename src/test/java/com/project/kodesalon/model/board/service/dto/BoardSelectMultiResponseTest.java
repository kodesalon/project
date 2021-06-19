package com.project.kodesalon.model.board.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.project.kodesalon.model.member.domain.MemberTest.TEST_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;

public class BoardSelectMultiResponseTest {
    private static final int TEST_BOARD_RESPONSE_SIZE = 10;

    @Test
    @DisplayName("게시물 조회 결과 리스트를 반환한다.")
    void getter() {
        BoardSelectMultiResponse boardSelectMultiResponse = new BoardSelectMultiResponse(createBoardResponses());

        then(boardSelectMultiResponse.getBoards().size()).isEqualTo(TEST_BOARD_RESPONSE_SIZE);
    }

    private List<BoardSelectSingleResponse> createBoardResponses() {
        List<BoardSelectSingleResponse> boardSelectSingleResponses = new ArrayList<>();

        for (int i = 0; i < TEST_BOARD_RESPONSE_SIZE; i++) {
            boardSelectSingleResponses.add(new BoardSelectSingleResponse("게시물 제목", "게시물 내용", LocalDateTime.now().toString(), TEST_MEMBER.getAlias()));
        }

        return boardSelectSingleResponses;
    }
}
