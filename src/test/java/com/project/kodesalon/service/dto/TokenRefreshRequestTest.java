package com.project.kodesalon.service.dto;

import com.project.kodesalon.service.dto.request.TokenRefreshRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.project.kodesalon.exception.ErrorCode.INVALID_JWT_TOKEN;
import static org.assertj.core.api.BDDAssertions.then;

class TokenRefreshRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("Refresh 토큰 값을 반환한다.")
    void getter() {
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest("token");
        then(tokenRefreshRequest.getRefreshToken()).isEqualTo("token");
    }

    @Test
    @DisplayName("null일 경우 예외가 발생한다.")
    void validateNull() {
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(null);
        Set<ConstraintViolation<TokenRefreshRequest>> constraintViolations = validator.validate(tokenRefreshRequest);
        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_JWT_TOKEN);
    }
}
