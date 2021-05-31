package com.project.kodesalon.model.board.domain.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BoardCreateRequestDtoTest {

    @Test
    @DisplayName("게시물의 제목, 내용, 작성자, 생성 시간, 삭제 여부를 반환한다.")
    public void getter() {
        BoardCreateRequestDto boardCreateRequestDto = new BoardCreateRequestDto(1L, "제목", "내용", "생성 시간");
        assertAll(
                () -> assertThat(boardCreateRequestDto.getMemberId()).isEqualTo(1L),
                () -> assertThat(boardCreateRequestDto.getTitle()).isEqualTo("제목"),
                () -> assertThat(boardCreateRequestDto.getContent()).isEqualTo("내용"),
                () -> assertThat(boardCreateRequestDto.getCreatedDateTime()).isEqualTo("생성 시간")
        );
    }
}
