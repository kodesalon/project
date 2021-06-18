package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;

class AliasTest {

    @Test
    @DisplayName("value 메서드를 호출하면 별명 값을 리턴합니다.")
    void value() {
        Alias alias = new Alias("alias");

        then(alias.value()).isEqualTo("alias");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "a12", "abcde12345abcde1", "alias 1234", "1234", "a_______", "한글Alias"})
    @DisplayName("올바르지 않은 형식의 별명일 경우, 예외가 발생합니다.")
    void alias_throw_exception_with_invalid_format(String invalidFormat) {
        thenIllegalArgumentException().isThrownBy(() -> new Alias(invalidFormat))
                .withMessageContaining("아이디는 영문으로 시작해야 하며 4자리 이상 15자리 이하의 영문 혹은 숫자가 포함되어야 합니다.");
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다")
    void alias_throw_exception_with_null(String nullArgument) {
        thenIllegalArgumentException().isThrownBy(() -> new Alias(nullArgument))
                .withMessageContaining("아이디는 영문으로 시작해야 하며 4자리 이상 15자리 이하의 영문 혹은 숫자가 포함되어야 합니다.");
    }

    @Test
    @DisplayName("동일한 별명(Alias)을 가진 객체를 비교할 경우, true를 리턴합니다")
    void equals() {
        Alias alias = new Alias("alias");

        then(alias).isEqualTo(new Alias("alias"));
    }
}
