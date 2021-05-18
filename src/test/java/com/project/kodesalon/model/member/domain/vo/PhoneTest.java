package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

class PhoneTest {
    private static final String VALID_PHONE_MIDDLE_NUMBER_LENGTH_FOUR = "010-2222-3333";
    private static final String VALID_PHONE_MIDDLE_NUMBER_LENGTH_THREE = "011-222-4444";
    private static final String PHONE_IDENTIFIER_NUMBER_UNDER_LENGTH = "01-2222-3333";
    private static final String PHONE_IDENTIFIER_NUMBER_OVER_LENGTH = "0101-2222-3333";
    private static final String INVALID_PHONE_IDENTIFIER_NUMBER_FORMAT = "777-2222-3333";
    private static final String PHONE_MIDDLE_NUMBER_UNDER_LENGTH = "010-1-3333";
    private static final String PHONE_MIDDLE_NUMBER_OVER_LENGTH = "010-11111-3333";
    private static final String PHONE_LAST_NUMBER_UNDER_LENGTH = "010-2222-7";
    private static final String PHONE_LAST_NUMBER_OVER_LENGTH = "010-2222-77777";
    private static final String PHONE_ERROR_MESSAGE = "핸드폰 번호는 [휴대폰 앞자리 번호]- 3자리 혹은 4자리 수 - 4자리수의 형식 이어야 합니다.";

    @ParameterizedTest
    @ValueSource(strings = {VALID_PHONE_MIDDLE_NUMBER_LENGTH_FOUR,
            VALID_PHONE_MIDDLE_NUMBER_LENGTH_THREE})
    @DisplayName("유효한 핸드폰 번호는 값을 초기화 시킵니다.")
    void validate_phone_init_value(String validPhoneNumber) {
        //given
        Phone phone = new Phone(validPhoneNumber);

        //then
        then(phone.getValue()).isEqualTo(validPhoneNumber);
    }

    @ParameterizedTest
    @ValueSource(strings = {PHONE_IDENTIFIER_NUMBER_UNDER_LENGTH,
            PHONE_IDENTIFIER_NUMBER_OVER_LENGTH, INVALID_PHONE_IDENTIFIER_NUMBER_FORMAT,
            PHONE_MIDDLE_NUMBER_UNDER_LENGTH, PHONE_MIDDLE_NUMBER_OVER_LENGTH,
            PHONE_LAST_NUMBER_UNDER_LENGTH, PHONE_LAST_NUMBER_OVER_LENGTH})
    @DisplayName("유효하지 않은 형식의 핸드폰 번호는 예외를 발생시킵니다.")
    void invalid_phone_throw_exception(String invalidPhoneNumber) {
        //then
        assertThatThrownBy(() -> new Phone(invalidPhoneNumber)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PHONE_ERROR_MESSAGE);
    }

}

