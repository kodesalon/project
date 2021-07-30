package com.project.kodesalon.service.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class ErrorResponseTest {

    @Test
    @DisplayName("Error Response를 생성하면 message를 초기화 합니다.")
    void create_error_response_init_message() {
        ErrorResponse errorResponse = new ErrorResponse("error message");

        then(errorResponse.getCode()).isEqualTo("error message");
    }
}
