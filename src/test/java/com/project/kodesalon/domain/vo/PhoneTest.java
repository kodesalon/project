package com.project.kodesalon.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.project.kodesalon.common.code.ErrorCode.INVALID_MEMBER_PHONE;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;

public class PhoneTest {
    @ParameterizedTest
    @ValueSource(strings = {"010-2222-3333", "011-222-4444"})
    @DisplayName("value 메서드를 호출하면 휴대폰 번호를 리턴합니다.")
    void value(String validPhoneNumber) {
        Phone phone = new Phone(validPhoneNumber);

        then(phone.value()).isEqualTo(validPhoneNumber);
    }

    @ParameterizedTest
    @ValueSource(strings = {"01-2222-3333", "0101-2222-3333", "777-2222-3333",
            "010-1-3333", "010-11111-3333", "010-2222-7", "010-2222-77777"})
    @DisplayName("유효하지 않은 형식의 핸드폰 번호는 예외를 발생시킵니다.")
    void phone_throw_exception_with_invalid_format(String invalidPhoneNumber) {
        thenIllegalArgumentException().isThrownBy(() -> new Phone(invalidPhoneNumber))
                .withMessage(INVALID_MEMBER_PHONE);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다.")
    void phone_throw_exception_with_null(String nullArgument) {
        thenIllegalArgumentException().isThrownBy(() -> new Phone(nullArgument))
                .withMessage(INVALID_MEMBER_PHONE);
    }

    @Test
    @DisplayName("동일한 휴대폰 번호를 가진 객체를 비교할 경우, true를 리턴합니다")
    void same_alias_value_return_true() {
        Phone phone = new Phone("010-1111-2222");

        then(phone).isEqualTo(new Phone("010-1111-2222"));
    }
}

