package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;


class NameTest {
    @ParameterizedTest
    @ValueSource(strings = {"김씨", "박하늘별님구름햇님보다사랑스러우리"})
    @DisplayName("유효한 이름은 값을 초기화 합니다.")
    void validate_name_init_value(String validName) {
        Name name = new Name(validName);

        then(name.getValue()).isEqualTo(validName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"김", "박하늘별님구름햇님보다사랑스러우리님",
            "엄 이", "엄~", "abc"})
    @DisplayName("유효하지 않은 이름은 예외를 발생시킵니다.")
    void invalid_name_throw_exception(String invalidName) {
        assertThatThrownBy(() -> new Name(invalidName)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name은 2자리 이상 17자리 이하의 한글이어야 합니다.");
    }
}
