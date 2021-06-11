package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class EmailTest {
    @ParameterizedTest
    @ValueSource(strings = {"email@email.com", "email1234@email.com"})
    @DisplayName("getValue 메서드를 호출하면 이메일의 값을 리턴합니다.")
    void get_value_return_value(String value) {
        Email email = new Email(value);

        then(email.value()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"email1234.com", "email1234@emailcom", "한국어1234@email.com"})
    @DisplayName("유효하지 않은 이메일은 예외를 발생시킵니다")
    void invalid_email_throw_exception(String value) {
        thenThrownBy(() -> new Email(value)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일은 이메일주소@회사.com 형식 이어야 합니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"email@email.com,true", "not_same@email.com,false"})
    @DisplayName("동일한 Alias 값이면 true를 리턴합니다")
    void same_alias_value_return_true(String comparedEmail, boolean expect) {
        Email email = new Email("email@email.com");

        then(email.equals(new Email(comparedEmail)))
                .isEqualTo(expect);
    }
}
