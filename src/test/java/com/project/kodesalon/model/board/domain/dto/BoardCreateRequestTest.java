package com.project.kodesalon.model.board.domain.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BoardCreateRequestTest {

    @Test
    @DisplayName("게시물의 제목, 내용, 작성자, 생성 시간, 삭제 여부를 반환한다.")
    public void getter() {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest(1L, "제목", "내용", "생성 시간");
        assertAll(
                () -> assertThat(boardCreateRequest.getMemberId()).isEqualTo(1L),
                () -> assertThat(boardCreateRequest.getTitle()).isEqualTo("제목"),
                () -> assertThat(boardCreateRequest.getContent()).isEqualTo("내용"),
                () -> assertThat(boardCreateRequest.getCreatedDateTime()).isEqualTo("생성 시간")
        );
    }
}
