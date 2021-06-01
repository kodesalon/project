package com.project.kodesalon.model.member.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class UnAuthorizedExceptionTest {
    @Test
    @DisplayName("예외 메세지를 인자로 받으면 예외 메세지가 초기화 됩니다.")
    void create_exception_init_message() {
        UnAuthorizedException unAuthorizedException = new UnAuthorizedException("error message");

        then(unAuthorizedException.getMessage()).isEqualTo("error message");
    }
}
