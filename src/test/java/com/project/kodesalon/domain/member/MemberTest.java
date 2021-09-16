package com.project.kodesalon.domain.member;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.member.vo.Password;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDateTime;

import static com.project.kodesalon.exception.ErrorCode.DUPLICATED_PASSWORD;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_PASSWORD;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.Mockito.mock;

class MemberTest {

    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    @Test
    @DisplayName("회원을 생성한다.")
    void create() {
        String alias = "alias";
        String password = "Password!!123";
        String name = "이름";
        String email = "email@email.com";
        String phone = "010-1234-5678";
        LocalDateTime createdDateTime = LocalDateTime.of(2021, 7, 16, 23, 59);

        Member member = new Member(alias, password, name, email, phone, createdDateTime);

        softly.then(member.getAlias()).isEqualTo(alias);
        softly.then(member.getPassword()).isEqualTo(password);
        softly.then(member.getName()).isEqualTo(name);
        softly.then(member.getEmail()).isEqualTo(email);
        softly.then(member.getPhone()).isEqualTo(phone);
        softly.then(member.getCreatedDateTime()).isEqualTo(createdDateTime);
        softly.then(member.getLastModifiedDateTime());
        softly.then(member.isDeleted()).isFalse();
        softly.assertAll();
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("휴대폰 정보 없이 회원을 생성한다.")
    void create_with_phone_null(String phone) {
        String alias = "alias";
        String password = "Password!!123";
        String name = "이름";
        String email = "email@email.com";
        LocalDateTime createdDateTime = LocalDateTime.of(2021, 7, 16, 23, 59);

        Member member = new Member(alias, password, name, email, phone, createdDateTime);

        softly.then(member.getAlias()).isEqualTo(alias);
        softly.then(member.getPassword()).isEqualTo(password);
        softly.then(member.getName()).isEqualTo(name);
        softly.then(member.getEmail()).isEqualTo(email);
        softly.then(member.getPhone()).isEqualTo("");
        softly.then(member.getCreatedDateTime()).isEqualTo(createdDateTime);
        softly.then(member.isDeleted()).isFalse();
        softly.assertAll();
    }

    @ParameterizedTest
    @CsvSource({"Password!!123, true", "Password!!1234, false"})
    @DisplayName("회원 비밀번호 일치 여부를 반환한다.")
    void has_same_password(String password, boolean expected) {
        Member member = new Member("alias", "Password!!123", "이름", "email@email.com",
                "010-1234-4444", LocalDateTime.of(2021, 7, 16, 23, 59));
        Password passwordToCompare = new Password(password);

        boolean hasSamePassword = member.hasSamePassword(passwordToCompare);

        then(hasSamePassword).isEqualTo(expected);
    }

    @Test
    @DisplayName("게시물을 추가한다.")
    void addBoard() {
        Board board = mock(Board.class);
        Member member = new Member("alias", "Password123!", "이름", "email@email.com",
                "010-1234-4444", LocalDateTime.of(2021, 7, 16, 23, 59));

        member.addBoard(board);

        then(member.getBoards().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("로그인 시, 비밀번호가 다른 경우 예외를 발생시킵니다")
    void login_throw_exception_with_different_password() {
        Member member = new Member("alias", "Password123!", "이름", "email@email.com",
                "010-1234-4444", LocalDateTime.of(2021, 7, 16, 23, 59));

        thenThrownBy(() -> member.login("Password123!!!"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_MEMBER_PASSWORD);
    }

    @Test
    @DisplayName("비밀번호를 변경한다.")
    void changePassword() {
        Member member = new Member("alias", "Password123!", "이름", "email@email.com",
                "010-1234-4444", LocalDateTime.of(2021, 7, 16, 23, 59));
        String newPassword = "ChangePassword1!";
        LocalDateTime lastModifiedDateTime = LocalDateTime.of(2021, 7, 16, 23, 59);

        member.changePassword(newPassword, lastModifiedDateTime);

        softly.then(member.getPassword()).isEqualTo(newPassword);
        softly.then(member.getLastModifiedDateTime()).isEqualTo(lastModifiedDateTime);
        softly.assertAll();
    }

    @Test
    @DisplayName("변경하려는 패스워드가 기존 패스워드가 중복일 경우 예외가 발생한다.")
    void changePassword_throw_error_with_exist_password() {
        Member member = new Member("alias", "Password123!", "이름", "email@email.com",
                "010-1234-4444", LocalDateTime.of(2021, 7, 16, 23, 59));

        String password = member.getPassword();

        thenIllegalArgumentException()
                .isThrownBy(() -> member.changePassword(password, LocalDateTime.now()))
                .withMessage(DUPLICATED_PASSWORD);
    }

    @Test
    @DisplayName("회원을 삭제한다.")
    void delete() {
        Member member = new Member("alias", "Password123!", "이름", "email@email.com",
                "010-1234-4444", LocalDateTime.of(2021, 7, 16, 23, 59));
        LocalDateTime deletedDateTime = LocalDateTime.now();

        member.delete(deletedDateTime);

        softly.then(member.isDeleted()).isTrue();
        softly.then(member.getDeletedDateTime()).isEqualTo(deletedDateTime);
        softly.assertAll();
    }
}
