package com.project.kodesalon.service.dto.request;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.project.kodesalon.exception.ErrorCode.INVALID_JWT_TOKEN;
import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;

class TokenRefreshRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("회원 식별 번호와 Refresh 토큰 값을 반환한다")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(1L, "token");

        softly.then(tokenRefreshRequest.getMemberId()).isEqualTo(1L);
        softly.then(tokenRefreshRequest.getRefreshToken()).isEqualTo("token");
        softly.assertAll();
    }

    @Test
    @DisplayName("회원 식별 번호가 존재하지 않을 경우 예외가 발생한다")
    void validate_member_id() {
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(null, "token");
        Set<ConstraintViolation<TokenRefreshRequest>> constraintViolations = validator.validate(tokenRefreshRequest);
        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(NOT_EXIST_MEMBER);
    }

    @Test
    @DisplayName("리프레시 토큰이 존재하지 않을 경우 예외가 발생한다")
    void validate_refresh_token() {
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(1L, null);
        Set<ConstraintViolation<TokenRefreshRequest>> constraintViolations = validator.validate(tokenRefreshRequest);
        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_JWT_TOKEN);
    }
}
