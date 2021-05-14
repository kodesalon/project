package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


class NameTest {
    private static final String NAME_EXCEPTION_MESSAGE= "잘못된 Name 형식입니다.";
    private static final String VALID_NAME_LENGTH_TWO = "김씨";
    private static final String VALID_NAME_LENGTH_SEVENTEEN = "박하늘별님구름햇님보다사랑스러우리";
    private static final String INVALID_NAME_LENGTH_ONE = "김";
    private static final String INVALID_NAME_LENGTH_EIGHTEEN = "박하늘별님구름햇님보다사랑스러우리님";
    private static final String INVALID_NAME_INCLUDE_BLANK = "엄 이";
    private static final String INVALID_NAME_INCLUDE_SPECIAL_SYMBOL = "엄~";
    private static final String INVALID_NAME_INCLUDE_NON_KOREAN = "abc";

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
