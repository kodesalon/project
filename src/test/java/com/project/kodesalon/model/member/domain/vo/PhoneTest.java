package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class PhoneTest {
    @ParameterizedTest
    @ValueSource(strings = {"010-2222-3333", "011-222-4444"})
    @DisplayName("유효한 핸드폰 번호는 값을 초기화 시킵니다.")
    void validate_phone_init_value(String validPhoneNumber) {
        Phone phone = new Phone(validPhoneNumber);

        then(phone.value()).isEqualTo(validPhoneNumber);
    }

    @ParameterizedTest
    @ValueSource(strings = {"01-2222-3333", "0101-2222-3333", "777-2222-3333",
            "010-1-3333", "010-11111-3333", "010-2222-7", "010-2222-77777"})
    @DisplayName("유효하지 않은 형식의 핸드폰 번호는 예외를 발생시킵니다.")
    void invalid_phone_throw_exception(String invalidPhoneNumber) {
        thenThrownBy(() -> new Phone(invalidPhoneNumber)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("핸드폰 번호는 [휴대폰 앞자리 번호]- 3자리 혹은 4자리 수 - 4자리수의 형식 이어야 합니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"010-1111-2222,true", "010-1234-5678,false"})
    @DisplayName("동일한 Phone 값이면 true를 리턴합니다")
    void same_alias_value_return_true(String comparedPhone, boolean expect) {
        Phone phone = new Phone("010-1111-2222");

        then(phone.equals(new Phone(comparedPhone)))
                .isEqualTo(expect);
    }
}

