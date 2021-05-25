package com.project.kodesalon.model.member.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberTest {
    private static final String ALIAS = "alias";
    private static final String PASSWORD = "Password!!123";
    private static final String NAME = "엄희상";
    private static final String EMAIL = "email@email.com";
    private static final String PHONE = "010-1234-4444";

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
}
