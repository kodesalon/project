package com.project.kodesalon.model.board.service.dto;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardDeleteRequestTest {

    @Test
    @DisplayName("회원 식별 번호, 게시물 식별 번호를 반환한다.")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(1L, 1L);

        softly.then(boardDeleteRequest.getMemberId()).isEqualTo(1L);
        softly.then(boardDeleteRequest.getBoardId()).isEqualTo(1L);
    }
}
