package com.project.kodesalon.model.member.service.dto;

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

import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_PASSWORD;
import static org.assertj.core.api.BDDAssertions.then;

public class ChangePasswordRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("변경하려는 비밀번호를 반환한다.")
    void getter() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("ChangePassword1!");
        then(changePasswordRequest.getPassword()).isEqualTo("ChangePassword1!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"!pass12", "!!Password1234567", "Password12",
            "!!Password", "!!password12", "!!PASSWORD12", "!비밀!pass1234"})
    @DisplayName("Password가 유효하지 않으면 예외를 발생시킨다.")
    void create_throws_exception_with_invalid_password(String invalidPassword) {
        ChangePasswordRequest changePasswordRequestDto = new ChangePasswordRequest(invalidPassword);
        Set<ConstraintViolation<ChangePasswordRequest>> constraintViolations = validator.validate(changePasswordRequestDto);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_PASSWORD);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Password가 Null이면 예외를 발생시킨다.")
    void create_throws_exception_with_null_password(String nullPassword) {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(nullPassword);
        Set<ConstraintViolation<ChangePasswordRequest>> constraintViolations = validator.validate(changePasswordRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_PASSWORD);
    }
}
