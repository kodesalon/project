package com.project.kodesalon.model.member.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.BDDAssertions.then;

class MemberTest {
    private static final String ALIAS = "alias";
    private static final String PASSWORD = "Password!!123";
    private static final String NAME = "엄희상";
    private static final String EMAIL = "email@email.com";
    private static final String PHONE = "010-1234-4444";
    private static final String CSV_DELIMITER = ",";
    private static final String INCORRECT_PASSWORD = "Password!!1234";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String INCORRECT_PASSWORD_TEST_CASE = INCORRECT_PASSWORD + CSV_DELIMITER + TRUE;
    private static final String CORRECT_PASSWORD_TEST_CASE = PASSWORD + CSV_DELIMITER + FALSE;

    private Member member;

    @BeforeEach
    void setup() {
        member = new Member(ALIAS, PASSWORD, NAME, EMAIL, PHONE);
    }

    @Test
    @DisplayName("Member 객체를 생성하면 Alias를 초기화 합니다.")
    void create_alias() {
        then(member.getAlias()).isEqualTo(ALIAS);
    }

    @Test
    @DisplayName("Member 객체를 생성하면 Password를 초기화 합니다.")
    void create_password() {
        then(member.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    @DisplayName("Member 객체를 생성하면 Name을 초기화 합니다.")
    void create_name() {
        then(member.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Member 객체를 생성하면 Email을 초기화 합니다.")
    void create_email() {
        then(member.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    @DisplayName(("Member 객체를 생성하면 Phone을 초기화 합니다."))
    void create_phone() {
        then(member.getPhone()).isEqualTo(PHONE);
    }

    @ParameterizedTest
    @CsvSource({INCORRECT_PASSWORD_TEST_CASE, CORRECT_PASSWORD_TEST_CASE})
    @DisplayName("Member의 비밀번호가 일치하지 않으면 true, 일치하면 false를 리턴합니다.")
    void is_incorrect_password(String password, boolean expected) {
        then(Member.isIncorrectPassword(password)).isEqualTo(expected);
    }
}
