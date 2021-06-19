package com.project.kodesalon.model.board.service.dto;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_ID;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_ID;
import static org.assertj.core.api.BDDAssertions.then;

class BoardDeleteRequestTest {
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    @DisplayName("회원 식별 번호, 게시물 식별 번호를 반환한다.")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(1L, 1L);

        softly.then(boardDeleteRequest.getMemberId()).isEqualTo(1L);
        softly.then(boardDeleteRequest.getBoardId()).isEqualTo(1L);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("회원 식별 번호에 null을 입력할 경우, 예외가 발생합니다")
    void create_throw_exception_with_null_member_id(Long nullArgument) {
        BoardDeleteRequest boardDeleteRequest
                = new BoardDeleteRequest(nullArgument, 1L);
        Set<ConstraintViolation<BoardDeleteRequest>> constraintViolations = validator.validate(boardDeleteRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_ID);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("게시물 식별 번호에 null을 입력할 경우, 예외가 발생합니다")
    void create_throw_exception_with_null_board_id(Long nullArgument) {
        BoardDeleteRequest boardDeleteRequest
                = new BoardDeleteRequest(1L, nullArgument);
        Set<ConstraintViolation<BoardDeleteRequest>> constraintViolations = validator.validate(boardDeleteRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_BOARD_ID);
    }
}
