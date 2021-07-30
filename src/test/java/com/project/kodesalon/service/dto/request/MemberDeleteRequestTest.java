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

class MemberDeleteRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("회원 탈퇴 시간을 반환한다.")
    void getDeletedDateTime() {
        LocalDateTime deletedDateTime = LocalDateTime.now();
        MemberDeleteRequest memberDeleteRequest = new MemberDeleteRequest(deletedDateTime);

        then(memberDeleteRequest.getDeletedDateTime()).isEqualTo(deletedDateTime);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("비밀번호 변경 시간이 없을 경우 예외를 발생시킨다.")
    void create_throws_exception_with_null_last_modified_date_time(LocalDateTime invalidDeletedDateTime) {
        MemberDeleteRequest memberDeleteRequest = new MemberDeleteRequest(invalidDeletedDateTime);
        Set<ConstraintViolation<MemberDeleteRequest>> constraintViolations = validator.validate(memberDeleteRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_DATE_TIME);
    }
}
