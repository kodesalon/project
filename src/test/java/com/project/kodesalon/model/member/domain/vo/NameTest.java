package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


class NameTest {

    @ParameterizedTest
    @ValueSource(strings = {})
    @DisplayName("유효한 이름은 값을 초기화 합니다.")
    void validate_name_init_value() {

    }

    @ParameterizedTest
    @ValueSource(strings = {})
    @DisplayName("유효하지 않은 이름은 예외를 발생시킵니다.")
    void invalid_name_throw_exception() {

    }
}
