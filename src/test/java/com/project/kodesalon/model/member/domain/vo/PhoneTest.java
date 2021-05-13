package com.project.kodesalon.model.member.domain.vo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

class PhoneTest {
    private static final String VALID_PHONE_MIDDLE_NUMBER_LENGTH_FOUR = "010-2222-3333";
    private static final String VALID_PHONE_MIDDLE_NUMBER_LENGTH_THREE = "011-222-4444";
    private static final String PHONE_IDENTIFIER_NUMBER_UNDER_LENGTH = "01-2222-3333";
    private static final String PHONE_IDENTIFIER_NUMBER_OVER_LENGTH = "0101-2222-3333";
    private static final String INVALID_PHONE_IDENTIFIER_NUMBER_FORMAT = "777-2222-3333";
    private static final String PHONE_MIDDLE_NUMBER_UNDER_LENGTH = "010-1-3333";
    private static final String PHONE_MIDDLE_NUMBER_OVER_LENGTH = "010-11111-3333";
    private static final String PHONE_LAST_NUMBER_UNDER_LENGTH = "010-2222-7";
    private static final String PHONE_LAST_NUMBER_OVER_LENGTH = "010-2222-7777";

    @ParameterizedTest
    @ValueSource(strings = {VALID_PHONE_MIDDLE_NUMBER_LENGTH_FOUR,
            VALID_PHONE_MIDDLE_NUMBER_LENGTH_THREE})
    void validate_phone_init_value(String validPhoneNumber) {
        //given
        Phone phone = new Phone(validPhoneNumber);

        //then
        then(phone.getValue()).isEqualTo(validPhoneNumber);
    }

    @ParameterizedTest
    @ValueSource(strings = {})
    void invalid_phone_throw_exception() {

    }

    @Test
    void get_value_return_value() {

    }
}

