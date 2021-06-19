package com.project.kodesalon.model.board.domain;

import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.domain.vo.Phone;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.project.kodesalon.common.ErrorCode.NOT_AUTHORIZED_MEMBER;
import static com.project.kodesalon.model.member.domain.MemberTest.TEST_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;

public class BoardTest {
    private final String title = "게시물 제목";
    private final String content = "게시물 내용";
    private final LocalDateTime createdDateTime = LocalDateTime.now();
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(new Title(title), new Content(content), TEST_MEMBER, createdDateTime);
    }

    @Test
    @DisplayName("게시물의 제목, 내용, 작성자, 생성 시간, 삭제 여부를 반환한다.")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        softly.then(board.getTitle()).isEqualTo(title);
        softly.then(board.getContent()).isEqualTo(content);
        softly.then(board.getWriter()).isEqualTo(TEST_MEMBER.getName());
        softly.then(board.getCreatedDateTime()).isEqualTo(createdDateTime);
        softly.then(board.isDeleted()).isFalse();
        softly.assertAll();
    }

    @Test
    @DisplayName("게시물 삭제 여부를 참으로 변경한다.")
    void delete() {
        board.delete(TEST_MEMBER);

        then(board.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("다른 회원이 게시물 삭제를 시도할 경우, 예외가 발생한다.")
    void delete_throw_exception_with_not_authorized_member() {
        Member stranger = new Member(new Alias("different"), new Password("Password!!123"), new Name("이름"), new Email("email@email.com"), new Phone("010-1234-4444"));

        thenIllegalArgumentException()
                .isThrownBy(() -> board.delete(stranger))
                .withMessage(NOT_AUTHORIZED_MEMBER);
    }
}
