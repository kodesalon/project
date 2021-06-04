package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;


class NameTest {
    @ParameterizedTest
    @ValueSource(strings = {"김씨", "박하늘별님구름햇님보다사랑스러우리"})
    @DisplayName("유효한 이름은 값을 초기화 합니다.")
    void validate_name_init_value(String validName) {
        Name name = new Name(validName);

        then(name.value()).isEqualTo(validName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"김", "박하늘별님구름햇님보다사랑스러우리님",
            "엄 이", "엄~", "abc"})
    @DisplayName("유효하지 않은 이름은 예외를 발생시킵니다.")
    void invalid_name_throw_exception(String invalidName) {
        thenThrownBy(() -> new Name(invalidName)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름은 2자리 이상 17자리 이하의 한글이어야 합니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"이름,true", "계이름,false"})
    @DisplayName("동일한 Name 값이면 true를 리턴합니다")
    void same_alias_value_return_true(String comparedName, boolean expect) {
        Name name = new Name("이름");

        then(name.equals(new Name(comparedName)))
                .isEqualTo(expect);
    }
}
