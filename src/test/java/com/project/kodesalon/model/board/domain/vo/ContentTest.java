package com.project.kodesalon.model.board.domain.vo;

import com.project.kodesalon.model.board.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThatThrownBy(() -> new Content(input))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("내용에 공백 아닌 1자 이상의 문자를 입력");
    }

    @Test
    @DisplayName("500자를 초과할 경우, 예외가 발생한다.")
    public void checkLength() {
        assertThatThrownBy(() -> new Content("1".repeat(501)))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("내용이 500자를 초과");
    }

    @Test
    @DisplayName("내용의 문자열 값을 반환한다.")
    public void value() {
        String value = content.value();
        assertThat(value).isEqualTo(contentValue);
    }
}
