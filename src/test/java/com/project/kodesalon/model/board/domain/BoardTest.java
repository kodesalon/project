package com.project.kodesalon.model.board.domain;

import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class BoardTest {
    private Board board;
    private final String title = "title";
    private final String content = "content";
    private final String writer =  "writer";
    private final LocalDateTime createdDateTime = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        board = Board.builder()
                .title(new Title(title))
                .content(new Content(content))
                .writer(writer)
                .createdDateTime(createdDateTime)
                .build();
    }

    @Test
    @DisplayName("게시물의 제목, 내용, 작성자, 생성 시간, 삭제 여부를 반환한다.")
    public void getter() {
        assertAll(
                () -> assertThat(board.getTitle()).isEqualTo(title),
                () -> assertThat(board.getContent()).isEqualTo(content),
                () -> assertThat(board.getWriter()).isEqualTo(writer),
                () -> assertThat(board.getCreatedDateTime()).isEqualTo(createdDateTime),
                () -> assertThat(board.isDeleted()).isFalse()
        );
    }
}

