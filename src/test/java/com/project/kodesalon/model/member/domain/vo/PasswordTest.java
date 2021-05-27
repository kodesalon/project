package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

class PasswordTest {
    private static final String PASSWORD_EXCEPTION_MESSAGE = "Password는 영어 소문자, 대문자, 숫자, 특수문자를 포함한 8자리이상 16자리 이하여야 합니다.";
    private static final String VALID_PASSWORD_LENGTH_EIGHT = "!!Pass12";
    private static final String VALID_PASSWORD_LENGTH_SIXTEEN = "!!Password123456";
    private static final String INVALID_PASSWORD_LENGTH_SEVEN = "!pass12";
    private static final String INVALID_PASSWORD_LENGTH_SEVENTEEN = "!!Password1234567";
    private static final String INVALID_PASSWORD_NON_INCLUDE_SPECIAL_SYMBOL = "Password12";
    private static final String INVALID_PASSWORD_NON_INCLUDE_NUMBER = "!!Password";
    private static final String INVALID_PASSWORD_NON_INCLUDE_UPPER_CASE = "!!password12";
    private static final String INVALID_PASSWORD_NON_INCLUDE_LOWER_CASE = "!!PASSWORD12";
    private static final String INVALID_PASSWORD_INCLUDE_NON_ALPHABET = "!비밀!pass1234";

    @ParameterizedTest
    @ValueSource(strings = {VALID_PASSWORD_LENGTH_EIGHT, VALID_PASSWORD_LENGTH_SIXTEEN})
    @DisplayName("유효한 비밀번호는 값을 초기화 합니다.")
    void valid_password_init_value(String validPassword) {
        Password password = new Password(validPassword);

        then(password.value()).isEqualTo(validPassword);
    }

    @ParameterizedTest
    @ValueSource(strings = {INVALID_PASSWORD_LENGTH_SEVEN, INVALID_PASSWORD_LENGTH_SEVENTEEN,
            INVALID_PASSWORD_NON_INCLUDE_SPECIAL_SYMBOL, INVALID_PASSWORD_NON_INCLUDE_NUMBER,
            INVALID_PASSWORD_NON_INCLUDE_UPPER_CASE, INVALID_PASSWORD_NON_INCLUDE_LOWER_CASE,
            INVALID_PASSWORD_INCLUDE_NON_ALPHABET})
    @DisplayName("유효하지 않은 비밀번호는 예외를 발생시킵니다.")
    void invalid_password_throw_exception(String invalidPassword) {
        assertThatThrownBy(() -> new Password(invalidPassword)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PASSWORD_EXCEPTION_MESSAGE);
    }
}
