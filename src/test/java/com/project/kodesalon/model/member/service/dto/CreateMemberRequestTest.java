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
import java.util.Set;

import static org.assertj.core.api.BDDAssertions.then;

class CreateMemberRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private final BDDSoftAssertions softly = new BDDSoftAssertions();
    private final CreateMemberRequest createMemberRequest
            = new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", "010-1111-2222");

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
        CreateMemberRequest createMemberRequest
                = new CreateMemberRequest(invalidAlias, "Password123!!", "이름", "email@email.com", "010-1111-2222");
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("아이디는 영문으로 시작해야 하며 4자리 이상 15자리 이하의 영문 혹은 숫자가 포함되어야 합니다.");
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_alias(String nullArgument) {
        CreateMemberRequest createMemberRequest
                = new CreateMemberRequest(nullArgument, "Password123!!", "이름", "email@email.com", "010-1111-2222");
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("null이 아닌 4자리 이상의 아이디를 입력해주세요.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"!pass12", "!!Password1234567", "Password12",
            "!!Password", "!!password12", "!!PASSWORD12", "!비밀!pass1234"})
    @DisplayName("올바르지 않은 양식의 비밀번호일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_invalid_password(String invalidPassword) {
        CreateMemberRequest createMemberRequest
                = new CreateMemberRequest("alias", invalidPassword, "이름", "email@email.com", "010-1111-2222");
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("비밀번호는 영어 소문자, 대문자, 숫자, 특수문자를 포함한 8자리이상 16자리 이하여야 합니다.");
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_password(String nullArgument) {
        CreateMemberRequest createMemberRequest
                = new CreateMemberRequest("alias", nullArgument, "이름", "email@email.com", "010-1111-2222");
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("null이 아닌 8자리 이상의 비밀번호를 입력해주세요.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"김", "박하늘별님구름햇님보다사랑스러우리님", "엄 이", "엄~", "abc"})
    @DisplayName("유효하지 않은 이름은 예외를 발생시킵니다.")
    void create_throw_exception_with_invalid_name(String invalidName) {
        CreateMemberRequest createMemberRequest
                = new CreateMemberRequest("alias", "Password123!!", invalidName, "email@email.com", "010-1111-2222");
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("이름은 2자리 이상 17자리 이하의 한글이어야 합니다.");
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_name(String nullArgument) {
        CreateMemberRequest createMemberRequest
                = new CreateMemberRequest("alias", "Password123!!", nullArgument, "email@email.com", "010-1111-2222");
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("null이 아닌 2자리 이상의 이름을 입력해주세요.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"id.domain", "id.domain.com", "id@domain..com", "id@domain@com", "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com"})
    @DisplayName("올바르지 않은 형식의 이메일일 경우, 예외가 발생합니다")
    void create_throw_exception_with_invalid_email(String invalidEmail) {
        CreateMemberRequest createMemberRequest
                = new CreateMemberRequest("alias", "Password123!!", "이름", invalidEmail, "010-1111-2222");
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("이메일은 id@domain.com과 같은 형식이어야 합니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("null 또는 아무것도 입력하지 않을 경우, 예외가 발생합니다")
    void create_throw_exception_with_null_email(String nullArgument) {
        CreateMemberRequest createMemberRequest
                = new CreateMemberRequest("alias", "Password123!!", "이름", nullArgument, "010-1111-2222");
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("null 또는 빈 공백이 아닌 이메일 주소를 입력해주세요.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"01-2222-3333", "0101-2222-3333", "777-2222-3333",
            "010-1-3333", "010-11111-3333", "010-2222-7", "010-2222-77777"})
    @DisplayName("유효하지 않은 형식의 핸드폰 번호는 예외를 발생시킵니다.")
    void create_throw_exception_with_invalid_phone(String invalidPhone) {
        CreateMemberRequest createMemberRequest
                = new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", invalidPhone);
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("휴대폰 번호는 [3자리 수] - [3 ~ 4자리 수] - [4자리 수]의 형식 이어야 합니다.");
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void create_throw_exception_with_null_phone(String nullArgument) {
        CreateMemberRequest createMemberRequest
                = new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", nullArgument);
        Set<ConstraintViolation<CreateMemberRequest>> constraintViolations = validator.validate(createMemberRequest);

        then(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("null이 아닌 휴대폰 번호를 입력해주세요.");
    }
}
