package com.project.kodesalon.service.dto.response;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

class BoardSelectResponseTest {

    @Test
    @DisplayName("제목, 내용, 생성 시간, 작성자 별명을 반환한다.")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        LocalDateTime createdDateTime = LocalDateTime.now();
        List<BoardImageResponse> boardImages = Collections.singletonList(new BoardImageResponse(1L, "localhost:8080/bucket/directory/image.jpeg"));
        BoardSelectResponse boardSelectResponse = new BoardSelectResponse(1L, "제목", "내용", createdDateTime, 1L, "alias", boardImages);

        softly.then(boardSelectResponse.getBoardId()).isEqualTo(1L);
        softly.then(boardSelectResponse.getTitle()).isEqualTo("제목");
        softly.then(boardSelectResponse.getContent()).isEqualTo("내용");
        softly.then(boardSelectResponse.getCreatedDateTime()).isEqualTo(createdDateTime);
        softly.then(boardSelectResponse.getWriterId()).isEqualTo(1L);
        softly.then(boardSelectResponse.getWriterAlias()).isEqualTo("alias");
        softly.assertAll();
    }
}
