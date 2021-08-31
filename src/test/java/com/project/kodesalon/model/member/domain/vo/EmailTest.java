package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_EMAIL;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;

public class EmailTest {
    @ParameterizedTest
    @ValueSource(strings = {"email@email.com", "email1234@email.com"})
    @DisplayName("value 메서드를 호출하면 이메일 값을 리턴합니다.")
    void value(String value) {
        Email email = new Email(value);

        then(email.value()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "id.domain", "id.domain.com", "id@domain..com", "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com"})
    @DisplayName("올바르지 않은 형식의 이메일일 경우, 예외가 발생합니다")
    void email_throw_exception_with_invalid_format(String value) {
        thenIllegalArgumentException().isThrownBy(() -> new Email(value))
                .withMessage(INVALID_MEMBER_EMAIL);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다")
    void email_throw_exception_with_null(String nullArgument) {
        thenIllegalArgumentException().isThrownBy(() -> new Email(nullArgument))
                .withMessage(INVALID_MEMBER_EMAIL);
    }

    @Test
    @DisplayName("동일한 이메일 주소를 가진 객체를 비교할 경우, true를 리턴합니다")
    void equals() {
        Email email = new Email("email@email.com");

        then(email).isEqualTo(new Email("email@email.com"));
    }
}
