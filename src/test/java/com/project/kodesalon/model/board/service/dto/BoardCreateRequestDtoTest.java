package com.project.kodesalon.model.board.service.dto;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BoardCreateRequestDtoTest {
    private final BoardCreateRequestDto boardCreateRequestDto = new BoardCreateRequestDto(1L, "게시물 제목", "게시물 내용", LocalDateTime.parse("2021-06-01T23:59:59.999999"));

    @Test
    @DisplayName("게시물의 제목, 내용, 작성자, 생성 시간, 삭제 여부를 반환한다.")
    public void getter() {
        assertAll(
                () -> assertThat(boardCreateRequestDto.getMemberId()).isEqualTo(1L),
                () -> assertThat(boardCreateRequestDto.getTitle()).isEqualTo(new Title("게시물 제목")),
                () -> assertThat(boardCreateRequestDto.getContent()).isEqualTo(new Content("게시물 내용")),
                () -> assertThat(boardCreateRequestDto.getCreatedDateTime()).isEqualTo("2021-06-01T23:59:59.999999")
        );
    }

    @Test
    @DisplayName("작성자를 입력받아, 멤버 객체를 반환한다.")
    public void toBoard() {
        Board board = boardCreateRequestDto.toBoard("writer");
        assertAll(
                () -> assertThat(board.getTitle()).isEqualTo(new Title("게시물 제목")),
                () -> assertThat(board.getContent()).isEqualTo(new Content("게시물 내용")),
                () -> assertThat(board.getCreatedDateTime()).isEqualTo("2021-06-01T23:59:59.999999"),
                () -> assertThat(board.getWriter()).isEqualTo("writer")
        );
    }
}
