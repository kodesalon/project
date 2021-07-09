package com.project.kodesalon.model.board.service.dto;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class BoardSelectResponseTest {

    @Test
    @DisplayName("제목, 내용, 생성 시간, 작성자 별명을 반환한다.")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        String createdDateTime = LocalDateTime.now().toString();
        BoardSelectResponse boardSelectResponse = new BoardSelectResponse("제목", "내용", createdDateTime, 1L);

        softly.then(boardSelectResponse.getTitle()).isEqualTo("제목");
        softly.then(boardSelectResponse.getContent()).isEqualTo("내용");
        softly.then(boardSelectResponse.getCreatedDateTime()).isEqualTo(createdDateTime);
        softly.then(boardSelectResponse.getWriter()).isEqualTo(1L);
        softly.assertAll();
    }
}
