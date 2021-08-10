package com.project.kodesalon.model.member.service.dto;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class SelectMemberOwnBoardResponseTest {

    @Test
    @DisplayName("게시물의 식별 번호, 제목, 내용, 생성날짜를 입력받아 값을 반환한다.")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        LocalDateTime createdDateTime = LocalDateTime.now();
        SelectMemberOwnBoardResponse ownBoard = new SelectMemberOwnBoardResponse(1L, "게시물 제목", "게시물 내용", createdDateTime);

        softly.then(ownBoard.getBoardId()).isEqualTo(1L);
        softly.then(ownBoard.getTitle()).isEqualTo("게시물 제목");
        softly.then(ownBoard.getContent()).isEqualTo("게시물 내용");
        softly.then(ownBoard.getCreatedDateTime()).isEqualTo(createdDateTime);
        softly.assertAll();
    }
}
