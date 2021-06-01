package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

class PasswordTest {
    @ParameterizedTest
    @ValueSource(strings = {"!!Pass12", "!!Password123456"})
    @DisplayName("유효한 비밀번호는 값을 초기화 합니다.")
    void valid_password_init_value(String validPassword) {
        Password password = new Password(validPassword);

        then(password.value()).isEqualTo(validPassword);
    }

    @ParameterizedTest
    @ValueSource(strings = {"!pass12", "!!Password1234567", "Password12",
            "!!Password", "!!password12", "!!PASSWORD12", "!비밀!pass1234"})
    @DisplayName("유효하지 않은 비밀번호는 예외를 발생시킵니다.")
    void invalid_password_throw_exception(String invalidPassword) {
        assertThatThrownBy(() -> new Password(invalidPassword)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호는 영어 소문자, 대문자, 숫자, 특수문자를 포함한 8자리이상 16자리 이하여야 합니다.");
    }
}
