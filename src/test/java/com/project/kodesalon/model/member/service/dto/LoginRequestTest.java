package com.project.kodesalon.model.member.service.dto;

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

import static org.assertj.core.api.BDDAssertions.then;

class LoginRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("Alias와 Password를 반환한다.")
    void getter() {
        LoginRequest loginRequest = new LoginRequest("alias", "Password1234!!");
        BDDSoftAssertions softly = new BDDSoftAssertions();

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
                .contains("아이디는 영문으로 시작해야 하며 4자리 이상 15자리 이하의 영문 혹은 숫자가 포함되어야 합니다.");
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_alias(String nullArgument) {
        LoginRequest loginRequestWithNullAlias = new LoginRequest(nullArgument, "Password1!");
        Set<ConstraintViolation<LoginRequest>> constraintViolations = validator.validate(loginRequestWithNullAlias);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("null이 아닌 4자리 이상의 아이디를 입력해주세요.");
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
                .contains("비밀번호는 영어 소문자, 대문자, 숫자, 특수문자를 포함한 8자리이상 16자리 이하여야 합니다.");
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_password(String nullArgument) {
        LoginRequest loginRequestWithNullPassword = new LoginRequest("alias", nullArgument);
        Set<ConstraintViolation<LoginRequest>> constraintViolations = validator.validate(loginRequestWithNullPassword);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("null이 아닌 8자리 이상의 비밀번호를 입력해주세요.");
    }
}
