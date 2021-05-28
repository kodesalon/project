package com.project.kodesalon.model.member.domain;


import com.project.kodesalon.model.member.domain.vo.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberTest {
    private static final String ALIAS = "alias";
    private static final String PASSWORD = "Password!!123";
    private static final String NAME = "엄희상";
    private static final String EMAIL = "email@email.com";
    private static final String PHONE = "010-1234-4444";
    private static final String INVALID_PASSWORD = "Invalid123!!";

    private Member member;

    @BeforeEach
    void setup() {
        member = new Member(ALIAS, PASSWORD, NAME, EMAIL, PHONE);
    }

    @Test
    @DisplayName(("Member 객체를 생성하면 필드를 초기화 합니다."))
    void create_member_init_filed() {
        assertAll(
                () -> then(member.getAlias()).isEqualTo(ALIAS),
                () -> then(member.getPassword()).isEqualTo(PASSWORD),
                () -> then(member.getName()).isEqualTo(NAME),
                () -> then(member.getEmail()).isEqualTo(EMAIL),
                () -> then(member.getPhone()).isEqualTo(PHONE)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {PASSWORD + ",false", INVALID_PASSWORD + ",true"})
    @DisplayName("Member의 비밀번호와 일치하지 않는다면 true, 일치하면 false를 리턴합니다.")
    void incorrect_password_return_true(String password, boolean expect) {
        then(member.isIncorrectPassword(new Password(password)))
                .isEqualTo(expect);
    }
}
