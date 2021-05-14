package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {
    private static final String PASSWORD_EXCEPTION_MESSAGE = "";
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
