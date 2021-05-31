package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;


class AliasTest {
    @Test
    @DisplayName("value 메서드를 호출하면 별명의 값을 리턴합니다.")
    void value() {
        Alias alias = new Alias("alias");

        then(alias.getValue()).isEqualTo("alias");
    }

    @ParameterizedTest
    @ValueSource(strings = {"aa", "alias1234alias1234alias1234",
            "alias 1234", "1234", "a_______", "한글Alias"})
    @DisplayName("alias에 타당한 문자열 포맷이 아니면 예외를 리턴합니다.")
    void invalidateFormatAlias_throw_exception(String invalidFormat) {
        assertThatThrownBy(() -> new Alias(invalidFormat)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Alias 는 영문으로 시작해야 하며 4자리 이상 15자리 이하의 영문 혹은 숫자가 포함되어야 합니다.");
    }
}
