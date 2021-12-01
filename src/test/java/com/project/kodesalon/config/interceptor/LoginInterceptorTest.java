package com.project.kodesalon.config.interceptor;

import org.hibernate.SessionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static com.project.kodesalon.config.interceptor.LoginInterceptor.LOGIN_MEMBER;
import static com.project.kodesalon.exception.ErrorCode.INVALID_SESSION;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoginInterceptorTest {

    @InjectMocks
    private LoginInterceptor loginInterceptor;

    @Spy
    private MockHttpServletRequest request;

    @Spy
    private MockHttpServletResponse response;

    @Spy
    private MockMvcResultHandlers handler;

    @Spy
    private MockHttpSession session;

    @Test
    @DisplayName("Session이 존재하고, 회원 정보를 포함한다면 참을 반환한다.")
    void preHandle() {
        session.setAttribute(LOGIN_MEMBER, 1L);
        request.setSession(session);

        boolean expect = loginInterceptor.preHandle(request, response, handler);

        then(expect).isTrue();
    }

    @Test
    @DisplayName("Preflight 요청일 경우, 참을 반환한다.")
    void preHandle_with_preflight() {
        request.setMethod("OPTIONS");

        boolean expect = loginInterceptor.preHandle(request, response, handler);

        then(expect).isTrue();
    }

    @Test
    @DisplayName("Session이 존재하지 않을 경우, 예외를 발생시킨다.")
    void preHandle_throws_exception_with_not_exist_session() {
        thenThrownBy(() -> loginInterceptor.preHandle(request, response, handler))
                .isInstanceOf(SessionException.class)
                .hasMessage(INVALID_SESSION);
    }

    @Test
    @DisplayName("Session이 존재하지만, 회원 정보를 포함하지 않을 경우, 예외를 발생시킨다.")
    void preHandle_throws_exception_with_not_member_id() {
        request.setSession(session);

        thenThrownBy(() -> loginInterceptor.preHandle(request, response, handler))
                .isInstanceOf(SessionException.class)
                .hasMessage(INVALID_SESSION);
    }

    @Test
    @DisplayName("Http 요청이 끝난 후에 url, 로그 정보를 확인한다.")
    void afterCompletion() {
        loginInterceptor.afterCompletion(request, response, handler, null);

        verify(request, times(1)).getRequestURI();
    }
}
