package com.project.kodesalon.domain.vo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.project.kodesalon.common.code.ErrorCode.INVALID_BOARD_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class ContentTest {
    private String contentValue;
    private Content content;

    @BeforeEach
    void setUp() {
        contentValue = "게시물 내용";
        content = new Content(contentValue);
    }

    @Test
    @DisplayName("문자열을 입력받아 내용 객체를 생성한다.")
    public void create() {
        assertThat(content).isEqualTo(new Content(contentValue));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("공백 또는 아무것도 입력하지 않을 경우, 예외가 발생한다.")
    public void checkNullOrBlank(String input) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Content(input))
                .withMessage(INVALID_BOARD_CONTENT);
    }

    @Test
    @DisplayName("500자를 초과할 경우, 예외가 발생한다.")
    public void checkLength() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Content("1".repeat(501)))
                .withMessage(INVALID_BOARD_CONTENT);
    }

    @Test
    @DisplayName("내용의 문자열 값을 반환한다.")
    public void value() {
        String value = content.value();
        assertThat(value).isEqualTo(contentValue);
    }
}
