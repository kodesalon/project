package com.project.kodesalon.model.member.domain;


import com.project.kodesalon.model.member.domain.vo.Password;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class MemberTest {
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

        softly.assertAll();
    }

    @ParameterizedTest
    @CsvSource({"Password!!1234,false", "Password!!123,true"})
    @DisplayName("Member의 비밀번호가 일치하면 true, 일치하지 않으면 false를 리턴합니다.")
    void is_incorrect_password(String password, boolean expected) {
        then(member.hasSamePassword(new Password(password))).isEqualTo(expected);
    }

    @Test
    @DisplayName("로그인 시, 비밀번호가 다른 경우 로그인에 실패하면 예외를 발생시킵니다")
    void login_throw_exception_with_different_password() {
        thenThrownBy(() -> member.login("Password123!!!"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀 번호가 일치하지 않습니다.");
    }
}
