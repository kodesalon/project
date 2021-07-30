package com.project.kodesalon.service.dto.request;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static com.project.kodesalon.exception.ErrorCode.INVALID_DATE_TIME;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_PASSWORD;
import static org.assertj.core.api.BDDAssertions.then;

class MemberChangePasswordRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    @Test
    @DisplayName("변경하려는 비밀번호를 반환한다.")
    void getter() {
        LocalDateTime lastModifiedDateTime = LocalDateTime.now();
        MemberChangePasswordRequest memberChangePasswordRequest = new MemberChangePasswordRequest("ChangePassword1!", lastModifiedDateTime);

        softly.then(memberChangePasswordRequest.getPassword()).isEqualTo("ChangePassword1!");
        softly.then(memberChangePasswordRequest.getLastModifiedDateTime()).isEqualTo(lastModifiedDateTime);
        softly.assertAll();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"!pass12", "!!Password1234567", "Password12",
            "!!Password", "!!password12", "!!PASSWORD12", "!비밀!pass1234"})
    @DisplayName("비밀 번호가 유효하지 않으면 예외를 발생시킨다.")
    void create_throws_exception_with_invalid_password(String invalidPassword) {
        MemberChangePasswordRequest memberChangePasswordRequestDto = new MemberChangePasswordRequest(invalidPassword, LocalDateTime.now());
        Set<ConstraintViolation<MemberChangePasswordRequest>> constraintViolations = validator.validate(memberChangePasswordRequestDto);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_PASSWORD);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("비밀번호 변경 시간이 없을 경우 예외를 발생시킨다.")
    void create_throws_exception_with_null_last_modified_date_time(LocalDateTime invalidLastModifiedDateTime) {
        MemberChangePasswordRequest memberChangePasswordRequestDto = new MemberChangePasswordRequest("ChangePassword1!", invalidLastModifiedDateTime);
        Set<ConstraintViolation<MemberChangePasswordRequest>> constraintViolations = validator.validate(memberChangePasswordRequestDto);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_DATE_TIME);
    }
}
