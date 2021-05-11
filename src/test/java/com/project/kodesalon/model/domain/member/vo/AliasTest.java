package com.project.kodesalon.model.domain.member.vo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;


class AliasTest {
    public static final String ALIAS = "alias";
    private Alias alias;

    @BeforeEach
    void setup() {
        alias = new Alias(ALIAS);
    }

    @Test
    @DisplayName("value 메서드를 호출하면 별명의 값을 리턴합니다.")
    void value() {
        //when
        String value = alias.value();

        //then
        then(value).isEqualTo(ALIAS);
    }
}
