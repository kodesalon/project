package com.project.kodesalon.service.dto.response;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardImageResponseTest {

    @Test
    @DisplayName("이미지 식별 번호, 이미지 url을 인자로 받아 값을 반환한다.")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        BoardImageResponse boardImageResponse = new BoardImageResponse(1L, "localhost:8080/bucket/directory/image.jpeg");

        softly.then(boardImageResponse.getImageId()).isEqualTo(1L);
        softly.then(boardImageResponse.getImageUrl()).isEqualTo("localhost:8080/bucket/directory/image.jpeg");
        softly.assertAll();
    }
}
