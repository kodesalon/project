package com.project.kodesalon.model.member.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class MemberTest {
    private Member member;
    @BeforeEach
    void setup() {
        member = new Member("alias", "Password!!123", "email@email.com", "010-1234-4444", "엄희상");
    }

    @Test
    @DisplayName("Member 객체를 생성하면 Alias를 초기화 합니다.")
    void create_alias() {
        then(member.getAlias()).isEqualto("alias");
    }
}
