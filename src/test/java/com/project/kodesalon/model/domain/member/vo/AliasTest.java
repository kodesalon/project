package com.project.kodesalon.model.domain.member.vo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;


class AliasTest {
    private static final String ALIAS = "alias";
    private static final String UNDER_LENGTH = "aa";
    private static final String OVER_LENGTH = "alias1234alias1234alias1234";
    private static final String INCLUDE_BLANK = "alias 1234";
    private static final String NOT_START_WITH_ALPHABET = "1234";
    private static final String INCLUDE_SPECIAL_SYMBOL = "a_______";
    private static final String INVALID_ALIAS_EXCEPTION_MESSAGE = "잘못된 Alias 형식입니다.";
    private static final String INCLUDE_NON_ALPHABET = "한글Alias";

    private Alias alias;

    @BeforeEach
    void setup() {
        alias = new Alias(ALIAS);
    }

    @Test
    @DisplayName("value 메서드를 호출하면 별명의 값을 리턴합니다.")
    void value() {
        //when
        String value = alias.getValue();

        //then
        then(value).isEqualTo(ALIAS);
    }

    @ParameterizedTest
    @ValueSource(strings = {UNDER_LENGTH, OVER_LENGTH,
            INCLUDE_BLANK, NOT_START_WITH_ALPHABET,
            INCLUDE_SPECIAL_SYMBOL, INCLUDE_NON_ALPHABET})
    @DisplayName("alias에 타당한 문자열 포맷이 아니면 예외를 리턴합니다.")
    void invalidateFormatAlias_throw_exception(String invalidFormat) {
        //then
        assertThatThrownBy(() -> new Alias(invalidFormat)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining(INVALID_ALIAS_EXCEPTION_MESSAGE);
    }
}
