package com.project.kodesalon.domain.member.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_NAME;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;


class NameTest {

    @ParameterizedTest
    @ValueSource(strings = {"김씨", "박하늘별님구름햇님보다사랑스러우리"})
    @DisplayName("value 메서드를 호출하면 이름을 리턴합니다.")
    void value(String validName) {
        Name name = new Name(validName);

        then(name.value()).isEqualTo(validName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"김", "박하늘별님구름햇님보다사랑스러우리님", "엄 이", "엄~", "abc"})
    @DisplayName("유효하지 않은 이름은 예외를 발생시킵니다.")
    void name_throw_exception_with_invalid_format(String invalidName) {
        thenIllegalArgumentException().isThrownBy(() -> new Name(invalidName))
                .withMessage(INVALID_MEMBER_NAME);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("null일 경우, 예외가 발생합니다")
    void name_throw_exception_with_null(String nullArgument) {
        thenIllegalArgumentException().isThrownBy(() -> new Name(nullArgument))
                .withMessage(INVALID_MEMBER_NAME);

    }

    @Test
    @DisplayName("동일한 이름을 가진 객체를 비교할 경우, true를 리턴합니다")
    void equals() {
        Name name = new Name("이름");

        then(name).isEqualTo(new Name("이름"));
    }
}
