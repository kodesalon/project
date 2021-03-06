package com.project.kodesalon.service.dto.request;

import com.project.kodesalon.domain.member.Member;
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
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_ALIAS;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_EMAIL;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_NAME;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_PASSWORD;
import static org.assertj.core.api.BDDAssertions.then;

class MemberCreateRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    @Test
    @DisplayName("별명, 비밀번호, 이름, 이메일, 휴대폰 번호를 반환한다.")
    void getter() {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", "Password123!!", "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());

        softly.then(memberCreateRequest.getAlias()).isEqualTo("alias");
        softly.then(memberCreateRequest.getPassword()).isEqualTo("Password123!!");
        softly.then(memberCreateRequest.getName()).isEqualTo("이름");
        softly.then(memberCreateRequest.getEmail()).isEqualTo("email@email.com");
        softly.then(memberCreateRequest.getPhone()).isEqualTo("010-1234-5678");
        softly.assertAll();
    }

    @Test
    @DisplayName("회원 객체를 반환한다.")
    void toMember() {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", "Password123!!", "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());

        Member member = memberCreateRequest.toMember();

        softly.then(member.getAlias()).isEqualTo("alias");
        softly.then(member.getPassword()).isEqualTo("Password123!!");
        softly.then(member.getName()).isEqualTo("이름");
        softly.then(member.getEmail()).isEqualTo("email@email.com");
        softly.then(member.getPhone()).isEqualTo("010-1234-5678");
        softly.assertAll();
    }

    @Test
    @DisplayName("회원 객체를 반환한다.")
    void toMember_with_phone_null() {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", "Password123!!", "이름", "email@email.com", null, LocalDateTime.now());

        Member member = memberCreateRequest.toMember();

        softly.then(member.getAlias()).isEqualTo("alias");
        softly.then(member.getPassword()).isEqualTo("Password123!!");
        softly.then(member.getName()).isEqualTo("이름");
        softly.then(member.getEmail()).isEqualTo("email@email.com");
        softly.then(member.getPhone()).isEqualTo("");
        softly.assertAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a12", "abcde12345abcde1", "alias 1234", "1234", "a_______", "한글Alias"})
    @DisplayName("아이디(alias)의 유효성 검증이 실패할 경우 예외가 발생합니다.")
    void create_throw_exception_with_invalid_alias(String invalidAlias) {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest(invalidAlias, "Password123!!", "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());
        Set<ConstraintViolation<MemberCreateRequest>> constraintViolations = validator.validate(memberCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_ALIAS);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("아이디(alias)가 null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_alias(String nullArgument) {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest(nullArgument, "Password123!!", "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());
        Set<ConstraintViolation<MemberCreateRequest>> constraintViolations = validator.validate(memberCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_ALIAS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"!pass12", "!!Password1234567", "Password12",
            "!!Password", "!!password12", "!!PASSWORD12", "!비밀!pass1234"})
    @DisplayName("올바르지 않은 양식의 비밀번호일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_invalid_password(String invalidPassword) {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", invalidPassword, "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());
        Set<ConstraintViolation<MemberCreateRequest>> constraintViolations = validator.validate(memberCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_PASSWORD);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("비밀번호가 null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_password(String nullArgument) {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", nullArgument, "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());
        Set<ConstraintViolation<MemberCreateRequest>> constraintViolations = validator.validate(memberCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_PASSWORD);
    }

    @ParameterizedTest
    @ValueSource(strings = {"김", "박하늘별님구름햇님보다사랑스러우리님", "엄 이", "엄~", "abc"})
    @DisplayName("유효하지 않은 이름은 예외를 발생시킵니다.")
    void create_throw_exception_with_invalid_name(String invalidName) {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", "Password123!!", invalidName, "email@email.com", "010-1234-5678", LocalDateTime.now());
        Set<ConstraintViolation<MemberCreateRequest>> constraintViolations = validator.validate(memberCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_NAME);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("이름이 null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_name(String nullArgument) {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", "Password123!!", nullArgument, "email@email.com", "010-1234-5678", LocalDateTime.now());
        Set<ConstraintViolation<MemberCreateRequest>> constraintViolations = validator.validate(memberCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_NAME);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id.domain", "id.domain.com", "id@domain..com", "id@domain@com", "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com"})
    @DisplayName("올바르지 않은 형식의 이메일일 경우, 예외가 발생합니다")
    void create_throw_exception_with_invalid_email(String invalidEmail) {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", "Password123!!", "이름", invalidEmail, "010-1234-5678", LocalDateTime.now());
        Set<ConstraintViolation<MemberCreateRequest>> constraintViolations = validator.validate(memberCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_EMAIL);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("이메일에 null 또는 아무것도 입력하지 않을 경우, 예외가 발생합니다")
    void create_throw_exception_with_null_email(String nullArgument) {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", "Password123!!", "이름", nullArgument, "010-1234-5678", LocalDateTime.now());
        Set<ConstraintViolation<MemberCreateRequest>> constraintViolations = validator.validate(memberCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_EMAIL);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("생성 시간이 null일 경우, 예외가 발생합니다")
    void create_throws_exception_with_null_created_date_time(LocalDateTime createdDateTime) {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", "Password123!!", "이름", "email@email.com", "010-1234-5678", createdDateTime);
        Set<ConstraintViolation<MemberCreateRequest>> constraintViolations = validator.validate(memberCreateRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_DATE_TIME);
    }
}
