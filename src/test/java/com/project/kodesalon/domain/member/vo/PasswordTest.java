package com.project.kodesalon.domain.member.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_PASSWORD;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;

class PasswordTest {
    @ParameterizedTest
    @ValueSource(strings = {"!!Pass12", "!!Password123456"})
    @DisplayName("value 메서드를 호출하면 비밀번호를 리턴합니다.")
    void value(String validPassword) {
        Password password = new Password(validPassword);

        then(password.value()).isEqualTo(validPassword);
    }

    @ParameterizedTest
    @ValueSource(strings = {"!pass12", "!!Password1234567", "Password12",
            "!!Password", "!!password12", "!!PASSWORD12", "!비밀!pass1234"})
    @DisplayName("올바르지 않은 양식의 비밀번호일 경우, 예외가 발생합니다.")
    void password_throw_exception_with_invalid_format(String invalidPassword) {
        thenIllegalArgumentException().isThrownBy(() -> new Password(invalidPassword))
                .withMessage(INVALID_MEMBER_PASSWORD);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void password_throw_exception_with_null(String invalidPassword) {
        thenIllegalArgumentException().isThrownBy(() -> new Password(invalidPassword))
                .withMessage(INVALID_MEMBER_PASSWORD);
    }

    @Test
    @DisplayName("동일한 비밀번호 값을 가지는 객체를 비교할 경우, true를 리턴합니다")
    void equals() {
        Password password = new Password("Password123!!");

        then(password).isEqualTo(new Password("Password123!!"));
    }
}
