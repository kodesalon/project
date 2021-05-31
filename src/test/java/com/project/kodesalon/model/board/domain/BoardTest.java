package com.project.kodesalon.model.board.domain;

import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BoardTest {
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String WRITER = "writer";
    public static final LocalDateTime CREATED_DATE_TIME = LocalDateTime.parse("2021-06-01T23:59:59.999999");
    public static final Board TEST_BOARD = Board.builder()
            .title(new Title(TITLE))
            .content(new Content(CONTENT))
            .writer(WRITER)
            .createdDateTime(CREATED_DATE_TIME)
            .build();

    @Test
    @DisplayName("게시물의 제목, 내용, 작성자, 생성 시간, 삭제 여부를 반환한다.")
    public void getter() {
        assertAll(
                () -> assertThat(TEST_BOARD.getTitle()).isEqualTo(TITLE),
                () -> assertThat(TEST_BOARD.getContent()).isEqualTo(CONTENT),
                () -> assertThat(TEST_BOARD.getWriter()).isEqualTo(WRITER),
                () -> assertThat(TEST_BOARD.getCreatedDateTime()).isEqualTo(CREATED_DATE_TIME),
                () -> assertThat(TEST_BOARD.isDeleted()).isFalse()
        );
    }
}

