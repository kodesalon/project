package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class AliasTest {
    @Test
    @DisplayName("value 메서드를 호출하면 별명의 값을 리턴합니다.")
    void value() {
        Alias alias = new Alias("alias");

        then(alias.value()).isEqualTo("alias");
    }

    @ParameterizedTest
    @ValueSource(strings = {"aa", "alias1234alias1234alias1234",
            "alias 1234", "1234", "a_______", "한글Alias"})
    @DisplayName("alias에 타당한 문자열 포맷이 아니면 예외를 리턴합니다.")
    void invalidateFormatAlias_throw_exception(String invalidFormat) {
        thenThrownBy(() -> new Alias(invalidFormat)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("아이디는 영문으로 시작해야 하며 4자리 이상 15자리 이하의 영문 혹은 숫자가 포함되어야 합니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"alias,true", "notSame,false"})
    @DisplayName("동일한 Alias 값이면 true를 리턴합니다")
    void same_alias_value_return_true(String comparedAlias, boolean expect) {
        Alias alias = new Alias("alias");

        then(alias.equals(new Alias(comparedAlias)))
                .isEqualTo(expect);
    }
}
