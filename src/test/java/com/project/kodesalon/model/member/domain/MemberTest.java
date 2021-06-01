package com.project.kodesalon.model.member.domain;


import com.project.kodesalon.model.member.domain.vo.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberTest {
    private Member member;

    @BeforeEach
    void setup() {
        member = new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444");
    }

    @Test
    @DisplayName("Member 객체를 생성하면 각 필드가 초기화 됩니다.")
    void create_member_init_filed() {
        assertAll(
                () -> then(member.getAlias()).isEqualTo("alias"),
                () -> then(member.getPassword()).isEqualTo("Password!!123"),
                () -> then(member.getName()).isEqualTo("이름"),
                () -> then(member.getEmail()).isEqualTo("email@email.com"),
                () -> then(member.getPhone()).isEqualTo("010-1234-4444")
        );
    }

    @ParameterizedTest
    @CsvSource({"Password!!1234,true", "Password!!123,false"})
    @DisplayName("Member의 비밀번호가 일치하지 않으면 true, 일치하면 false를 리턴합니다.")
    void is_incorrect_password(String password, boolean expected) {
        then(member.isIncorrectPassword(new Password(password))).isEqualTo(expected);
    }
}
