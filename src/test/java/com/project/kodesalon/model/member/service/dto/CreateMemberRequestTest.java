package com.project.kodesalon.model.member.service.dto;

import com.project.kodesalon.model.member.domain.Member;
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

import static com.project.kodesalon.common.ErrorCode.INVALID_DATE_TIME;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_ALIAS;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_EMAIL;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_NAME;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_PASSWORD;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_PHONE;
import static org.assertj.core.api.BDDAssertions.then;

public class CreateMemberRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private final BDDSoftAssertions softly = new BDDSoftAssertions();
    private final CreateMemberRequest createMemberRequest =
            new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", "010-1111-2222", LocalDateTime.now());

    @Test
    @DisplayName("별명, 비밀번호, 이름, 이메일, 휴대폰 번호를 반환한다.")
    void getter() {
        softly.then(createMemberRequest.getAlias()).isEqualTo("alias");
        softly.then(createMemberRequest.getPassword()).isEqualTo("Password123!!");
        softly.then(createMemberRequest.getName()).isEqualTo("이름");
        softly.then(createMemberRequest.getEmail()).isEqualTo("email@email.com");
        softly.then(createMemberRequest.getPhone()).isEqualTo("010-1111-2222");
        softly.assertAll();
    }

    @Test
    @DisplayName("toMember()를 호출하면 Member 객체가 반환합니다")
    void toMember() {
        Member member = createMemberRequest.toMember();

        softly.then(member.getAlias()).isEqualTo("alias");
        softly.then(member.getPassword()).isEqualTo("Password123!!");
        softly.then(member.getName()).isEqualTo("이름");
        softly.then(member.getEmail()).isEqualTo("email@email.com");
        softly.then(member.getPhone()).isEqualTo("010-1111-2222");
        softly.assertAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a12", "abcde12345abcde1", "alias 1234", "1234", "a_______", "한글Alias"})
    @DisplayName("alias에 타당한 문자열 포맷이 아니면 예외가 발생합니다.")
    void create_throw_exception_with_invalid_alias(String invalidAlias) {
        CreateMemberRequest createMemberRequest =
                new CreateMemberRequest(invalidAlias, "Password123!!", "이름", "email@email.com", "010-1111-2222", LocalDateTime.now());
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_ALIAS);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_alias(String nullArgument) {
        CreateMemberRequest createMemberRequest =
                new CreateMemberRequest(nullArgument, "Password123!!", "이름", "email@email.com", "010-1111-2222", LocalDateTime.now());
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_ALIAS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"!pass12", "!!Password1234567", "Password12",
            "!!Password", "!!password12", "!!PASSWORD12", "!비밀!pass1234"})
    @DisplayName("올바르지 않은 양식의 비밀번호일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_invalid_password(String invalidPassword) {
        CreateMemberRequest createMemberRequest =
                new CreateMemberRequest("alias", invalidPassword, "이름", "email@email.com", "010-1111-2222", LocalDateTime.now());
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_PASSWORD);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_password(String nullArgument) {
        CreateMemberRequest createMemberRequest =
                new CreateMemberRequest("alias", nullArgument, "이름", "email@email.com", "010-1111-2222", LocalDateTime.now());
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_PASSWORD);
    }

    @ParameterizedTest
    @ValueSource(strings = {"김", "박하늘별님구름햇님보다사랑스러우리님", "엄 이", "엄~", "abc"})
    @DisplayName("유효하지 않은 이름은 예외를 발생시킵니다.")
    void create_throw_exception_with_invalid_name(String invalidName) {
        CreateMemberRequest createMemberRequest =
                new CreateMemberRequest("alias", "Password123!!", invalidName, "email@email.com", "010-1111-2222", LocalDateTime.now());
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_NAME);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_name(String nullArgument) {
        CreateMemberRequest createMemberRequest =
                new CreateMemberRequest("alias", "Password123!!", nullArgument, "email@email.com", "010-1111-2222", LocalDateTime.now());
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_NAME);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id.domain", "id.domain.com", "id@domain..com", "id@domain@com", "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com"})
    @DisplayName("올바르지 않은 형식의 이메일일 경우, 예외가 발생합니다")
    void create_throw_exception_with_invalid_email(String invalidEmail) {
        CreateMemberRequest createMemberRequest =
                new CreateMemberRequest("alias", "Password123!!", "이름", invalidEmail, "010-1111-2222", LocalDateTime.now());
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_EMAIL);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("null 또는 아무것도 입력하지 않을 경우, 예외가 발생합니다")
    void create_throw_exception_with_null_email(String nullArgument) {
        CreateMemberRequest createMemberRequest =
                new CreateMemberRequest("alias", "Password123!!", "이름", nullArgument, "010-1111-2222", LocalDateTime.now());
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_EMAIL);
    }

    @ParameterizedTest
    @ValueSource(strings = {"01-2222-3333", "0101-2222-3333", "777-2222-3333",
            "010-1-3333", "010-11111-3333", "010-2222-7", "010-2222-77777"})
    @DisplayName("유효하지 않은 형식의 핸드폰 번호는 예외를 발생시킵니다.")
    void create_throw_exception_with_invalid_phone(String invalidPhone) {
        CreateMemberRequest createMemberRequest =
                new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", invalidPhone, LocalDateTime.now());
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_PHONE);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_phone(String nullArgument) {
        CreateMemberRequest createMemberRequest =
                new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", nullArgument, LocalDateTime.now());
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_MEMBER_PHONE);
    }

    @ParameterizedTest
    @NullSource
    void create_throws_exception_with_null_created_date_time(LocalDateTime createdDateTime) {
        CreateMemberRequest createMemberRequest =
                new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", "010-1111-2222", createdDateTime);
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains(INVALID_DATE_TIME);
    }
}
