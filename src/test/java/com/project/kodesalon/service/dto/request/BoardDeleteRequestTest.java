package com.project.kodesalon.service.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static com.project.kodesalon.exception.ErrorCode.INVALID_DATE_TIME;
import static org.assertj.core.api.BDDAssertions.then;

class BoardDeleteRequestTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    @DisplayName("삭제 시간을 반환한다.")
    void getter() {
        LocalDateTime deletedDateTime = LocalDateTime.now();

        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(deletedDateTime);

        then(boardDeleteRequest.getDeletedDateTime()).isEqualTo(deletedDateTime);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("삭제 시간이 null일 경우, 예외가 발생한다.")
    void create_throw_exception_with_null_deleted_date_time(LocalDateTime deletedDateTime) {
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(deletedDateTime);

        Set<ConstraintViolation<BoardDeleteRequest>> constraintViolations = validator.validate(boardDeleteRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_DATE_TIME);
    }
}
