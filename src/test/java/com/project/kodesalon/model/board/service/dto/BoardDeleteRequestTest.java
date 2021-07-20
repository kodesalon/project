package com.project.kodesalon.model.board.service.dto;

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

import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_ID;
import static com.project.kodesalon.common.ErrorCode.INVALID_DATE_TIME;
import static org.assertj.core.api.BDDAssertions.then;

public class BoardDeleteRequestTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    @DisplayName("게시물 번호, 삭제 시간을 반환한다.")
    void getter() {
        LocalDateTime deletedDateTime = LocalDateTime.now();
        Long boardId = 1L;

        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(boardId, deletedDateTime);

        then(boardDeleteRequest.getBoardId()).isEqualTo(boardId);
        then(boardDeleteRequest.getDeletedDateTime()).isEqualTo(deletedDateTime);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("게시물 번호가 null일 경우, 예외가 발생한다.")
    void create_throw_exception_with_null_board_id(Long boardId) {
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(boardId, LocalDateTime.now());

        Set<ConstraintViolation<BoardDeleteRequest>> constraintViolations = validator.validate(boardDeleteRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_BOARD_ID);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("삭제 시간이 null일 경우, 예외가 발생한다.")
    void create_throw_exception_with_null_deleted_date_time(LocalDateTime deletedDateTime) {
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(1L, deletedDateTime);

        Set<ConstraintViolation<BoardDeleteRequest>> constraintViolations = validator.validate(boardDeleteRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_DATE_TIME);
    }
}
