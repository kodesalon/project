package com.project.kodesalon.model.domain.member.vo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.IllegalFormatException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;


class AliasTest {
    public static final String ALIAS = "alias1234";
    public static final String MINIMUM_LENGTH_ALIAS = "";
    public static final String BLANK_INCLUDE_ALIAS = "alias 1234";
    public static final String NUMBER_NOT_INCLUDED = "alias";
    public static final String NOT_START_WITH_ALPHABET = "1234";

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

    @Test
    @DisplayName("alias 올바른 형식이면 alias 리턴합니다")
    void validateAlias()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        Method method = alias.getClass().getDeclaredMethod("validateAlias", String.class);
        method.setAccessible(true);

        //when
        String result = (String) method.invoke(alias, ALIAS);

        //then
        then(result).isEqualTo(ALIAS);
    }

    @ParameterizedTest
    @ValueSource(strings = {MINIMUM_LENGTH_ALIAS, BLANK_INCLUDE_ALIAS, NUMBER_NOT_INCLUDED, NOT_START_WITH_ALPHABET})
    @DisplayName("alias에 타당한 문자열 포맷이 아니면 예외를 리턴합니다.")
    void invalidateFormatAlias_throw_exception(String invalidFormat)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //given
        Method validateAliasMethod = alias.getClass().getDeclaredMethod("validateAlias", String.class);
        validateAliasMethod.setAccessible(true);
        //when

        //then
        assertThatThrownBy(() -> validateAliasMethod.invoke(alias, invalidFormat)).isInstanceOf(IllegalFormatException.class)
                .hasMessageContaining("잘못된 Alias 형식입니다.");
    }
}

