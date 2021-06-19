package com.project.kodesalon.model.board.domain;

import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.project.kodesalon.model.member.domain.MemberTest.TEST_MEMBER;

public class BoardTest {
    private final String title = "게시물 제목";
    private final String content = "게시물 내용";
    private final LocalDateTime createdDateTime = LocalDateTime.now();
    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(new Title(title), new Content(content), TEST_MEMBER, createdDateTime);
    }

    @Test
    @DisplayName("게시물의 제목, 내용, 작성자, 생성 시간, 삭제 여부를 반환한다.")
    public void getter() {
        softly.then(board.getTitle()).isEqualTo(title);
        softly.then(board.getContent()).isEqualTo(content);
        softly.then(board.getWriter()).isEqualTo(TEST_MEMBER.getName());
        softly.then(board.getCreatedDateTime()).isEqualTo(createdDateTime);
        softly.then(board.isDeleted()).isFalse();
        softly.assertAll();
    }

    @Test
    @DisplayName("게시물의 제목과 내용을 전달받아 board의 제목과 내용을 변경한다.")
    void update_board() {
        Title updateTitle = new Title("update title");
        Content updateContent = new Content("update content");

        board.updateTitleAndContent(updateTitle, updateContent);

        softly.then(board.getTitle()).isEqualTo(updateTitle.value());
        softly.then(board.getContent()).isEqualTo(updateContent.value());
        softly.assertAll();
    }
}

