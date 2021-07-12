package com.project.kodesalon.model.member.domain;

import com.project.kodesalon.model.member.domain.vo.Password;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.project.kodesalon.common.ErrorCode.DUPLICATED_PASSWORD;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_PASSWORD;
import static com.project.kodesalon.model.board.domain.BoardTest.TEST_BOARD;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

public class MemberTest {
    public static final Member TEST_MEMBER
            = new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444");

    private Member member;

    @BeforeEach
    void setup() {
        member = new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444");
    }

    @Test
    @DisplayName("Member 객체를 생성하면 각 필드가 초기화 됩니다.")
    void create_member_init_filed() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        softly.then(member.getAlias()).isEqualTo("alias");
        softly.then(member.getPassword()).isEqualTo("Password!!123");
        softly.then(member.getName()).isEqualTo("이름");
        softly.then(member.getEmail()).isEqualTo("email@email.com");
        softly.then(member.getPhone()).isEqualTo("010-1234-4444");
        softly.then(member.isDeleted()).isFalse();
        softly.assertAll();
    }

    @ParameterizedTest
    @CsvSource({"Password!!123,true", "Password!!1234,false"})
    @DisplayName("Member의 비밀번호가 일치하면 true, 일치하지 않으면 false를 리턴합니다.")
    void has_same_password(String password, boolean expected) {
        then(member.hasSamePassword(new Password(password))).isEqualTo(expected);
    }

    @Test
    @DisplayName("게시물을 추가한다.")
    void addBoard() {
        member.addBoard(TEST_BOARD);

        then(member.getBoards().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("로그인 시, 비밀번호가 다른 경우 로그인에 실패하면 예외를 발생시킵니다")
    void login_throw_exception_with_different_password() {
        thenThrownBy(() -> member.login("Password123!!!"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_MEMBER_PASSWORD);
    }

    @Test
    @DisplayName("비밀번호를 변경한다.")
    void changePassword() {
        String newPassword = "ChangePassword1!";

        member.changePassword(newPassword);

        then(member.getPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("변경하려는 패스워드가 기존 패스워드가 중복일 경우 예외가 발생한다.")
    void changePassword_throw_error_with_exist_password() {
        String password = member.getPassword();
        thenIllegalArgumentException()
                .isThrownBy(() -> member.changePassword(password))
                .withMessage(DUPLICATED_PASSWORD);
    }

    @Test
    @DisplayName("멤버를 삭제하면 isDeleted 속성이 true가 된다.")
    void delete() {
        member.delete();

        then(member.isDeleted()).isTrue();
    }
}
