package com.project.kodesalon.model.board.domain;

import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.project.kodesalon.model.member.domain.MemberTest.TEST_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;

public class BoardTest {
    private final String title = "게시물 제목";
    private final String content = "게시물 내용";
    private final LocalDateTime createdDateTime = LocalDateTime.now();
    private final Board TEST_BOARD = new Board(new Title(title), new Content(content), TEST_MEMBER, createdDateTime);

    @Test
    @DisplayName("게시물의 제목, 내용, 작성자, 생성 시간, 삭제 여부를 반환한다.")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        softly.then(TEST_BOARD.getTitle()).isEqualTo(title);
        softly.then(TEST_BOARD.getContent()).isEqualTo(content);
        softly.then(TEST_BOARD.getWriter()).isEqualTo(TEST_MEMBER.getName());
        softly.then(TEST_BOARD.getCreatedDateTime()).isEqualTo(createdDateTime);
        softly.then(TEST_BOARD.isDeleted()).isFalse();
        softly.assertAll();
    }

    @Test
    @DisplayName("게시물 삭제 여부를 참으로 변경한다.")
    void delete() {
        Board board = new Board(new Title(title), new Content(content), TEST_MEMBER, createdDateTime);
        board.delete();
        then(board.isDeleted()).isTrue();
    }
}
