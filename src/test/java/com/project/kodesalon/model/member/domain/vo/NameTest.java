package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;


class NameTest {
    private static final String NAME_EXCEPTION_MESSAGE = "Name은 2자리 이상 17자리 이하의 한글이어야 합니다.";
    private static final String VALID_NAME_LENGTH_TWO = "김씨";
    private static final String VALID_NAME_LENGTH_SEVENTEEN = "박하늘별님구름햇님보다사랑스러우리";
    private static final String INVALID_NAME_LENGTH_ONE = "김";
    private static final String INVALID_NAME_LENGTH_EIGHTEEN = "박하늘별님구름햇님보다사랑스러우리님";
    private static final String INVALID_NAME_INCLUDE_BLANK = "엄 이";
    private static final String INVALID_NAME_INCLUDE_SPECIAL_SYMBOL = "엄~";
    private static final String INVALID_NAME_INCLUDE_NON_KOREAN = "abc";

    @ParameterizedTest
    @ValueSource(strings = {VALID_NAME_LENGTH_TWO, VALID_NAME_LENGTH_SEVENTEEN})
    @DisplayName("유효한 이름은 값을 초기화 합니다.")
    void validate_name_init_value(String validName) {
        //given
        Name name = new Name(validName);

        //then
        then(name.getValue()).isEqualTo(validName);
    }

    @ParameterizedTest
    @ValueSource(strings = {INVALID_NAME_LENGTH_ONE, INVALID_NAME_LENGTH_EIGHTEEN,
            INVALID_NAME_INCLUDE_BLANK, INVALID_NAME_INCLUDE_SPECIAL_SYMBOL,
            INVALID_NAME_INCLUDE_NON_KOREAN})
    @DisplayName("유효하지 않은 이름은 예외를 발생시킵니다.")
    void invalid_name_throw_exception(String invalidName) {
        //then
        assertThatThrownBy(() -> new Name(invalidName)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(NAME_EXCEPTION_MESSAGE);
    }
}
