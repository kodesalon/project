package com.project.kodesalon.config.interceptor;

import com.project.kodesalon.service.JwtManager;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static com.project.kodesalon.config.interceptor.LoginInterceptor.LOGIN_MEMBER;
import static com.project.kodesalon.exception.ErrorCode.INVALID_HEADER;
import static com.project.kodesalon.exception.ErrorCode.INVALID_JWT_TOKEN;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoginInterceptorTest {

    @InjectMocks
    private LoginInterceptor loginInterceptor;

    @Mock
    private JwtManager jwtManager;

    @Spy
    private MockHttpServletRequest request;

    @Spy
    private MockHttpServletResponse response;

    @Spy
    private MockMvcResultHandlers handler;

    @BeforeEach
    void setUp() {
        request.addHeader("Authorization", "Bearer token");
    }

    @Test
    @DisplayName("request header에 jwt 토큰을 입력받아 회원 식별 번호를 attribute에 등록하고 true를 반환한다.")
    void preHandle() throws Exception {
        given(jwtManager.validateToken(anyString())).willReturn(true);
        given(jwtManager.getMemberIdFrom(anyString())).willReturn(1L);

        boolean expect = loginInterceptor.preHandle(request, response, handler);

        then(request.getAttribute(LOGIN_MEMBER)).isNotNull();
        then(expect).isTrue();
    }

    @Test
    @DisplayName("유효한 access token이 아닌 경우 예외를 발생시킨다.")
    void preHandle_throws_exception_with_invalid_jwt_token() {
        given(jwtManager.validateToken(anyString())).willThrow(new JwtException(INVALID_JWT_TOKEN));

        thenThrownBy(() -> loginInterceptor.preHandle(request, response, handler))
                .isInstanceOf(JwtException.class)
                .hasMessage(INVALID_JWT_TOKEN);
    }

    @Test
    @DisplayName("Header에 Authorizaiotn attribute가 없을 경우 예외를 발생시킨다.")
    void preHandle_throw_exception_with_null_authorized_header() {
        given(request.getHeader(anyString())).willThrow(new NullPointerException());

        thenThrownBy(() -> loginInterceptor.preHandle(request, response, handler))
                .isInstanceOf(JwtException.class)
                .hasMessage(INVALID_HEADER);
    }

    @Test
    @DisplayName("Http 요청이 끝난 후에 url, 로그 정보를 확인한다.")
    void afterCompletion() {
        loginInterceptor.afterCompletion(request, response, handler, null);

        verify(request, times(1)).getRequestURI();
    }
}
