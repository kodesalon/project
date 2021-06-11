package com.project.kodesalon.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class ErrorResponseTest {
    private static final String ERROR_MESSAGE = "error message";

    @Test
    @DisplayName("Error Response를 생성하면 message를 초기화 합니다.")
    void create_error_response_init_message() {
        ErrorResponse errorResponse = new ErrorResponse(ERROR_MESSAGE);

        then(errorResponse.getMessage()).isEqualTo(ERROR_MESSAGE);
    }
}
