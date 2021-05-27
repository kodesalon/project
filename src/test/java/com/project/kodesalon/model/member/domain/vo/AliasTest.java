package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;


class AliasTest {
    private static final String ALIAS = "alias";
    private static final String UNDER_LENGTH = "aa";
    private static final String OVER_LENGTH = "alias1234alias1234alias1234";
    private static final String INCLUDE_BLANK = "alias 1234";
    private static final String NOT_START_WITH_ALPHABET = "1234";
    private static final String INCLUDE_SPECIAL_SYMBOL = "a_______";
    private static final String INVALID_ALIAS_EXCEPTION_MESSAGE = "Alias 는 영문으로 시작해야 하며 4자리 이상 15자리 이하의 영문 혹은 숫자가 포함되어야 합니다.";
    private static final String INCLUDE_NON_ALPHABET = "한글Alias";

    private Alias alias;

    @BeforeEach
    void setup() {
        alias = new Alias(ALIAS);
    }

    @Test
    @DisplayName("value 메서드를 호출하면 별명의 값을 리턴합니다.")
    void value() {
        Alias alias = new Alias(ALIAS);

        then(alias.value()).isEqualTo(ALIAS);
    }

    @ParameterizedTest
    @ValueSource(strings = {UNDER_LENGTH, OVER_LENGTH,
            INCLUDE_BLANK, NOT_START_WITH_ALPHABET,
            INCLUDE_SPECIAL_SYMBOL, INCLUDE_NON_ALPHABET})
    @DisplayName("alias에 타당한 문자열 포맷이 아니면 예외를 리턴합니다.")
    void invalidateFormatAlias_throw_exception(String invalidFormat) {
        assertThatThrownBy(() -> new Alias(invalidFormat)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_ALIAS_EXCEPTION_MESSAGE);
    }
}
