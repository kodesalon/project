package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

class EmailTest {
    private static final String NORMAL_EMAIL = "email@email.com";
    private static final String NORMAL_EMAIL_INCLUDE_NUMBER = "email1234@email.com";
    private static final String NO_AT_EMAIL = "email1234.com";
    private static final String NO_DOT_EMAIL = "email1234@emailcom";
    private static final String NON_ALPHABET_EMAIL = "한국어1234@email.com";
    private static final String EMAIL_EXCEPTION_MESSAGE = "Email은 이메일주소@회사.com 형식 이어야 합니다.";
    private Email testEmail;

    @BeforeEach
    void setUp() {
        testEmail = new Email(NORMAL_EMAIL);
    }

    @ParameterizedTest
    @ValueSource(strings = {NORMAL_EMAIL, NORMAL_EMAIL_INCLUDE_NUMBER})
    @DisplayName("getValue 메서드를 호출하면 이메일의 값을 리턴합니다.")
    void get_value_return_value(String value) {
        Email email = new Email(value);

        then(email.value()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {NO_AT_EMAIL, NO_DOT_EMAIL, NON_ALPHABET_EMAIL})
    @DisplayName("유효하지 않은 이메일은 예외를 발생시킵니다")
    void invalid_email_throw_exception(String value) {
        assertThatThrownBy(() -> new Email(value)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(EMAIL_EXCEPTION_MESSAGE);
    }

    @ParameterizedTest
    @CsvSource(value = {NORMAL_EMAIL + ",true", NORMAL_EMAIL_INCLUDE_NUMBER + ",false"})
    @DisplayName("이메일 값이 같으면 true, 다르면 false를 리턴합니다.")
    void same_email_value_return_true(String email, boolean expect) {
        then(testEmail.equals(new Email(email))).isEqualTo(expect);
    }
}
