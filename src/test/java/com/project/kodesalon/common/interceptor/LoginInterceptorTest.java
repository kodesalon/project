package com.project.kodesalon.common.interceptor;

import com.project.kodesalon.common.JwtUtils;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
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

import static com.project.kodesalon.common.interceptor.LoginInterceptor.LOGIN_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoginInterceptorTest {

    @InjectMocks
    private LoginInterceptor loginInterceptor;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private MemberService memberService;

    @Mock
    private Member member;

    @Spy
    private MockHttpServletRequest request;

    @Spy
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request.addHeader("Authorization", "Bearer token");
    }

    @Test
    @DisplayName("request header에 jwt 토큰을 입력받아 Login할 멤버를 attribute에 등록하고 true를 반환한다.")
    void preHandle() throws Exception {
        given(jwtUtils.validateToken(anyString())).willReturn(true);
        given(jwtUtils.getMemberIdFrom(anyString())).willReturn(1L);
        given(memberService.findById(anyLong())).willReturn(member);

        boolean status = loginInterceptor.preHandle(request, response, null);

        then(status).isTrue();
        then(request.getAttribute(LOGIN_MEMBER)).isNotNull();
    }

    @Test
    @DisplayName("유효한 access token이 아닌 경우 예외를 발생시킨다.")
    void preHandle_throw_exception_with_expired_jwt_token() throws Exception {
        given(jwtUtils.validateToken(anyString())).willThrow(new JwtException("invalid access token"));

        thenThrownBy(() -> loginInterceptor.preHandle(request, response, null))
                .isInstanceOf(JwtException.class)
                .hasMessage("invalid access token");
    }

    @Test
    @DisplayName("Http 요청이 끝난 후에 로그 정보를 확인한다.")
    void afterCompletion() throws Exception {
        loginInterceptor.afterCompletion(request, response, null, null);
        verify(request, times(1)).getAttribute(anyString());
    }
}
