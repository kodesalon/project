package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {
    private static final String PASSWORD_EXCEPTION_MESSAGE = "";
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
    @ValueSource(strings = {})
    @DisplayName("유효한 비밀번호는 값을 초기화 합니다.")
    void valid_password_init_value() {

    }

    @ParameterizedTest
    @ValueSource(strings = {})
    @DisplayName("유효하지 않은 비밀번호는 예외를 발생시킵니다.")
    void invalid_password_throw_exception() {

    }
}
