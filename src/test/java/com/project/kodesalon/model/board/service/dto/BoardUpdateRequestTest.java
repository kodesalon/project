package com.project.kodesalon.model.board.service.dto;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_CONTENT;
import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_TITLE;
import static com.project.kodesalon.common.ErrorCode.INVALID_DATE_TIME;
import static com.project.kodesalon.model.board.domain.vo.Content.CONTENT_LENGTH_MAX_BOUND;
import static com.project.kodesalon.model.board.domain.vo.Title.TITLE_LENGTH_MAX_BOUND;
import static org.assertj.core.api.BDDAssertions.then;

public class BoardUpdateRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    @Test
    @DisplayName("수정할 게시물의 식별자, 변경할 게시물 제목, 변경할 게시물 내용을 반환합니다")
    void getter() {
        LocalDateTime lastModifiedDateTime = LocalDateTime.now();
        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest("update title", "update content", lastModifiedDateTime);

        softly.then(boardUpdateRequest.getTitle()).isEqualTo("update title");
        softly.then(boardUpdateRequest.getContent()).isEqualTo("update content");
        softly.then(boardUpdateRequest.getLastModifiedDateTime()).isEqualTo(lastModifiedDateTime);
        softly.assertAll();
    }

    @Test
    @DisplayName("업데이트 할 제목의 길이가 30자를 초과할 경우, 예외가 발생합니다")
    void create_throw_exception_with_invalid_title() {
        BoardUpdateRequest boardUpdateRequest
                = new BoardUpdateRequest("1".repeat(TITLE_LENGTH_MAX_BOUND + 1), "updated content", LocalDateTime.now());
        Set<ConstraintViolation<BoardUpdateRequest>> constraintViolations = validator.validate(boardUpdateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_BOARD_TITLE);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("업데이트 할 제목이 null이거나 아무것도 입력하지 않은 경우 예외가 발생합니다")
    void create_throw_exception_with_null_and_empty_title(String nullAndEmptyTitle) {
        BoardUpdateRequest boardUpdateRequest
                = new BoardUpdateRequest(nullAndEmptyTitle, "updated content", LocalDateTime.now());
        Set<ConstraintViolation<BoardUpdateRequest>> constraintViolations = validator.validate(boardUpdateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_BOARD_TITLE);
    }

    @Test
    @DisplayName("업데이트 할 내용이 500자를 초과할 경우, 예외가 발생합니다")
    void create_throw_exception_with_invalid_content() {
        BoardUpdateRequest boardUpdateRequest
                = new BoardUpdateRequest("update title", "1".repeat(CONTENT_LENGTH_MAX_BOUND + 1), LocalDateTime.now());
        Set<ConstraintViolation<BoardUpdateRequest>> constraintViolations = validator.validate(boardUpdateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_BOARD_CONTENT);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("업데이트 할 내용이 null이거나 아무것도 입력하지 않은 경우 예외가 발생합니다")
    void create_throw_exception_with_null_and_empty_content(String nullAndEmptyContent) {
        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest("updated title", nullAndEmptyContent, LocalDateTime.now());
        Set<ConstraintViolation<BoardUpdateRequest>> constraintViolations = validator.validate(boardUpdateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_BOARD_CONTENT);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("마지막으로 수정된 시간이 유효하지 않을 경우 예외를 발생합니다")
    void create_throw_exception_with_invalid_last_modified_date_time(LocalDateTime invalidLastModifiedDateTime) {
        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest("updated title", "updated content", invalidLastModifiedDateTime);
        Set<ConstraintViolation<BoardUpdateRequest>> constraintViolations = validator.validate(boardUpdateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_DATE_TIME);
    }
}
