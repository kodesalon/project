package com.project.kodesalon.model.board.service.dto;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class BoardSelectSingleResponseTest {

    @Test
    @DisplayName("제목, 내용, 생성 시간, 작성자 별명을 반환한다.")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        String createdDateTime = LocalDateTime.now().toString();
        BoardSelectSingleResponse boardSelectSingleResponse = new BoardSelectSingleResponse("제목", "내용", createdDateTime, "작성자 별명");

        softly.then(boardSelectSingleResponse.getTitle()).isEqualTo("제목");
        softly.then(boardSelectSingleResponse.getContent()).isEqualTo("내용");
        softly.then(boardSelectSingleResponse.getCreatedDateTime()).isEqualTo(createdDateTime);
        softly.then(boardSelectSingleResponse.getWriter()).isEqualTo("작성자 별명");
        softly.assertAll();
    }
}
