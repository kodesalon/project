package com.project.kodesalon.service.dto.request;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.member.Member;
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

import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_CONTENT;
import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_TITLE;
import static com.project.kodesalon.exception.ErrorCode.INVALID_DATE_TIME;
import static com.project.kodesalon.utils.TestEntityUtils.getTestMember;
import static org.assertj.core.api.BDDAssertions.then;

class BoardCreateRequestTest {

    private static final Member TEST_MEMBER = getTestMember();

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private final BDDSoftAssertions softly = new BDDSoftAssertions();
    private final BoardCreateRequest boardCreateRequest = new BoardCreateRequest("게시물 제목", "게시물 내용", LocalDateTime.parse("2021-06-01T23:59:59.999999"));

    @Test
    @DisplayName("게시물의 제목, 내용, 생성 시간, 삭제 여부를 반환한다.")
    void getter() {
        softly.then(boardCreateRequest.getTitle()).isEqualTo("게시물 제목");
        softly.then(boardCreateRequest.getContent()).isEqualTo("게시물 내용");
        softly.then(boardCreateRequest.getCreatedDateTime()).isEqualTo("2021-06-01T23:59:59.999999");
        softly.assertAll();
    }

    @Test
    @DisplayName("작성자를 입력받아, 멤버 객체를 반환한다.")
    void toBoard() {
        Board board = boardCreateRequest.toBoard(TEST_MEMBER);

        softly.then(board.getTitle()).isEqualTo("게시물 제목");
        softly.then(board.getContent()).isEqualTo("게시물 내용");
        softly.then(board.getCreatedDateTime()).isEqualTo("2021-06-01T23:59:59.999999");
        softly.then(board.getWriter()).isEqualTo(TEST_MEMBER);
        softly.assertAll();
    }

    @Test
    @DisplayName("제목 길이가 30자를 초과할 경우, 예외가 발생합니다")
    void create_throw_exception_with_invalid_title() {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest("현재 게시물 제목의 길이가 31자이므로 예외 발생합니다.", "게시물 내용", LocalDateTime.parse("2021-06-01T23:59:59.999999"));
        Set<ConstraintViolation<BoardCreateRequest>> constraintViolations = validator.validate(boardCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_BOARD_TITLE);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("null 또는 아무것도 입력하지 않을 경우, 예외가 발생합니다")
    void create_throw_exception_with_null_title(String nullArgument) {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest(nullArgument, "게시물 내용", LocalDateTime.parse("2021-06-01T23:59:59.999999"));
        Set<ConstraintViolation<BoardCreateRequest>> constraintViolations = validator.validate(boardCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_BOARD_TITLE);
    }

    @Test
    @DisplayName("내용의 500자를 초과할 경우, 예외가 발생합니다")
    void create_throw_exception_with_invalid_content() {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest("게시물 제목", "1".repeat(501), LocalDateTime.parse("2021-06-01T23:59:59.999999"));
        Set<ConstraintViolation<BoardCreateRequest>> constraintViolations = validator.validate(boardCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_BOARD_CONTENT);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("null 또는 아무것도 입력하지 않을 경우, 예외가 발생합니다")
    void create_throw_exception_with_null_content(String nullArgument) {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest("게시물 제목", nullArgument, LocalDateTime.parse("2021-06-01T23:59:59.999999"));
        Set<ConstraintViolation<BoardCreateRequest>> constraintViolations = validator.validate(boardCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_BOARD_CONTENT);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null 일 경우, 예외가 발생합니다")
    void create_throw_exception_with_null_created_date_time(LocalDateTime createdDateTime) {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest("게시물 제목", "게시물 내용", createdDateTime);
        Set<ConstraintViolation<BoardCreateRequest>> constraintViolations = validator.validate(boardCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_DATE_TIME);
    }
}
