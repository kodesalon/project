package com.project.kodesalon.model.board.domain;

import com.project.kodesalon.model.member.domain.Member;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.project.kodesalon.common.ErrorCode.ALREADY_DELETED_BOARD;
import static com.project.kodesalon.common.ErrorCode.NOT_AUTHORIZED_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.thenIllegalStateException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BoardTest {
    private final String title = "게시물 제목";
    private final String content = "게시물 내용";
    private final LocalDateTime createdDateTime = LocalDateTime.now();
    private Board board;

    @Mock
    private Member member;

    @Mock
    private Member stranger;

    @BeforeEach
    void setUp() {
        board = new Board(title, content, member, createdDateTime);
    }

    @Test
    @DisplayName("게시물의 제목, 내용, 작성자, 생성 시간, 삭제 여부를 반환한다.")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        softly.then(board.getTitle()).isEqualTo(title);
        softly.then(board.getContent()).isEqualTo(content);
        softly.then(board.getWriter()).isEqualTo(member);
        softly.then(board.getCreatedDateTime()).isEqualTo(createdDateTime);
        softly.then(board.isDeleted()).isFalse();
        softly.assertAll();
    }

    @Test
    @DisplayName("게시물 삭제 여부를 참으로 변경한다.")
    void delete() {
        given(member.getId()).willReturn(1L);

        board.delete(member.getId());

        then(board.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("다른 회원이 게시물 삭제를 시도할 경우, 예외가 발생한다.")
    void delete_throw_exception_with_not_authorized_member() {
        given(member.getId()).willReturn(1L);
        given(stranger.getId()).willReturn(2L);

        thenIllegalArgumentException()
                .isThrownBy(() -> board.delete(stranger.getId()))
                .withMessage(NOT_AUTHORIZED_MEMBER);
    }

    @Test
    @DisplayName("이미 삭제한 게시물을 다시 삭제 시도할 경우, 예외가 발생한다.")
    void delete_throw_exception_with_already_deleted() {
        board.delete(member.getId());

        thenIllegalStateException()
                .isThrownBy(() -> board.delete(member.getId()))
                .withMessage(ALREADY_DELETED_BOARD);
    }
}
