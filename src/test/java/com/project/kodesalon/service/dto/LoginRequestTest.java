package com.project.kodesalon.service.dto;

import com.project.kodesalon.service.dto.request.LoginRequest;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.project.kodesalon.common.code.ErrorCode.INVALID_MEMBER_ALIAS;
import static com.project.kodesalon.common.code.ErrorCode.INVALID_MEMBER_PASSWORD;
import static org.assertj.core.api.BDDAssertions.then;

class LoginRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("Alias와 Password를 반환한다.")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        LoginRequest loginRequest = new LoginRequest("alias", "Password1234!!");

        softly.then(loginRequest.getAlias()).isEqualTo("alias");
        softly.then(loginRequest.getPassword()).isEqualTo("Password1234!!");
        softly.assertAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a12", "abcde12345abcde1", "alias 1234", "1234", "a_______", "한글Alias"})
    @DisplayName("alias에 타당한 문자열 포맷이 아니면 예외가 발생합니다.")
    void create_throw_exception_with_invalid_Alias(String invalidFormat) {
        LoginRequest loginRequest = new LoginRequest(invalidFormat, "Password1!");
        Set<ConstraintViolation<LoginRequest>> constraintViolations = validator.validate(loginRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_ALIAS);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_alias(String nullArgument) {
        LoginRequest loginRequestWithNullAlias = new LoginRequest(nullArgument, "Password1!");
        Set<ConstraintViolation<LoginRequest>> constraintViolations = validator.validate(loginRequestWithNullAlias);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_ALIAS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"!pass12", "!!Password1234567", "Password12",
            "!!Password", "!!password12", "!!PASSWORD12", "!비밀!pass1234"})
    @DisplayName("올바르지 않은 양식의 비밀번호일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_invalid_password(String invalidPassword) {
        LoginRequest loginRequestWithInvalidPassword = new LoginRequest("alias", invalidPassword);
        Set<ConstraintViolation<LoginRequest>> constraintViolations = validator.validate(loginRequestWithInvalidPassword);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_PASSWORD);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_password(String nullArgument) {
        LoginRequest loginRequestWithNullPassword = new LoginRequest("alias", nullArgument);
        Set<ConstraintViolation<LoginRequest>> constraintViolations = validator.validate(loginRequestWithNullPassword);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_PASSWORD);
    }
}
