package com.project.kodesalon.model.board.domain;

import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BoardTest {
    private final String title = "게시물 제목";
    private final String content = "게시물 내용";
    private final String writer = "작성자";
    private final LocalDateTime createdDateTime = LocalDateTime.now();
    private final Board TEST_BOARD = new Board(new Title(title), new Content(content), writer, createdDateTime);

    @Test
    @DisplayName("게시물의 제목, 내용, 작성자, 생성 시간, 삭제 여부를 반환한다.")
    public void getter() {
        assertAll(
                () -> assertThat(TEST_BOARD.getTitle()).isEqualTo(title),
                () -> assertThat(TEST_BOARD.getContent()).isEqualTo(content),
                () -> assertThat(TEST_BOARD.getWriter()).isEqualTo(writer),
                () -> assertThat(TEST_BOARD.getCreatedDateTime()).isEqualTo(createdDateTime),
                () -> assertThat(TEST_BOARD.isDeleted()).isFalse()
        );
    }
}

