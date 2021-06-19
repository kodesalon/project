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
import java.util.Set;

import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_CONTENT;
import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_TITLE;
import static com.project.kodesalon.model.board.domain.vo.Content.CONTENT_LENGTH_BOUND_MAX;
import static com.project.kodesalon.model.board.domain.vo.Title.TITLE_LENGTH_MAX_BOUND;
import static org.assertj.core.api.BDDAssertions.then;

public class BoardUpdateRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    @Test
    @DisplayName("수정할 게시물의 식별자, 변경할 게시물 제목, 변경할 게시물 내용을 반환합니다")
    void getter() {
        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(1L, "update title", "update content");

        softly.then(boardUpdateRequest.getMemberId()).isEqualTo(1L);
        softly.then(boardUpdateRequest.getUpdatedTitle()).isEqualTo("update title");
        softly.then(boardUpdateRequest.getUpdatedContent()).isEqualTo("update content");
        softly.assertAll();
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("memberId가 null 또는 아무것도 입력하지 않을 경우, 예외가 발생합니다")
    void create_throw_exception_with_null_member_id(Long nullMemberId) {
        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(nullMemberId, "updated title", "updated content");
        Set<ConstraintViolation<BoardUpdateRequest>> constraintViolations = validator.validate(boardUpdateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("null이 아닌 회원 식별 번호를 입력해주세요.");
    }

    @Test
    @DisplayName("업데이트 할 제목의 길이가 30자를 초과할 경우, 예외가 발생합니다")
    void create_throw_exception_with_invalid_title() {
        BoardUpdateRequest boardUpdateRequest
                = new BoardUpdateRequest(1L, "1".repeat(TITLE_LENGTH_MAX_BOUND + 1), "updated content");
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
                = new BoardUpdateRequest(1L, nullAndEmptyTitle, "updated content");
        Set<ConstraintViolation<BoardUpdateRequest>> constraintViolations = validator.validate(boardUpdateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_BOARD_TITLE);
    }

    @Test
    @DisplayName("업데이트 할 내용이 500자를 초과할 경우, 예외가 발생합니다")
    void create_throw_exception_with_invalid_content() {
        BoardUpdateRequest boardUpdateRequest
                = new BoardUpdateRequest(1L, "update title", "1".repeat(CONTENT_LENGTH_BOUND_MAX + 1));
        Set<ConstraintViolation<BoardUpdateRequest>> constraintViolations = validator.validate(boardUpdateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_BOARD_CONTENT);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("업데이트 할 내용이 null이거나 아무것도 입력하지 않은 경우 예외가 발생합니다")
    void create_throw_exception_with_null_and_empty_content(String nullAndEmptyContent) {
        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(1L, "updated title", nullAndEmptyContent);
        Set<ConstraintViolation<BoardUpdateRequest>> constraintViolations = validator.validate(boardUpdateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_BOARD_CONTENT);
    }
}
