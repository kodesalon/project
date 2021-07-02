package com.project.kodesalon.common.argumentresolver;

import com.project.kodesalon.common.annotation.Login;
import com.project.kodesalon.model.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

import static com.project.kodesalon.common.interceptor.LoginInterceptor.LOGIN_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
class LoginMemberArgumentResolverTest {

    @Mock
    private MethodParameter parameter;

    @Mock
    private NativeWebRequest request;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Login annotationLogin;

    @Mock
    private Member member;

    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @BeforeEach
    void setUp() {
        loginMemberArgumentResolver = new LoginMemberArgumentResolver();
    }

    @Test
    @DisplayName("parameter가 Login annotation을 가지고 있고 Member 타입이면 true를 반환한다.")
    void supportsParameter() {
        given(parameter.hasParameterAnnotation(Login.class)).willReturn(true);
        willReturn(Member.class).given(parameter).getParameterType();

        then(loginMemberArgumentResolver.supportsParameter(parameter)).isTrue();
    }

    @Test
    @DisplayName("parameter가 Login annotation을 가지고 있지 않으면 false를 반환한다.")
    void supportsParameter_return_false_with_not_have_Login_annotation() {
        given(parameter.hasParameterAnnotation(Login.class)).willReturn(false);
        willReturn(Member.class).given(parameter).getParameterType();

        then(loginMemberArgumentResolver.supportsParameter(parameter)).isFalse();
    }

    @Test
    @DisplayName("parameter가 Member 타입이 아니면 false를 반환한다.")
    void supportsParameter_return_false_with_other_object() {
        given(parameter.hasParameterAnnotation(Login.class)).willReturn(true);
        willReturn(Object.class).given(parameter).getParameterType();

        then(loginMemberArgumentResolver.supportsParameter(parameter)).isFalse();
    }

    @Test
    @DisplayName("NativeWebRequest을 HttpServletRequest으로 변환 후 속성으로 받아온 Member 객체를 반환한다.")
    void resolveArgument() throws Exception {
        given(request.getNativeRequest()).willReturn(httpServletRequest);
        given(httpServletRequest.getAttribute(LOGIN_MEMBER)).willReturn(member);

        then(loginMemberArgumentResolver.resolveArgument(parameter, null, request, null))
                .isEqualTo(member);
    }
}
