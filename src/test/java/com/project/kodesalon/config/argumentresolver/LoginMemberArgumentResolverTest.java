package com.project.kodesalon.config.argumentresolver;

import com.project.kodesalon.config.argumentresolver.annotation.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.project.kodesalon.config.interceptor.LoginInterceptor.LOGIN_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
class LoginMemberArgumentResolverTest {

    @Mock
    private MethodParameter parameter;

    @Mock
    private NativeWebRequest webRequest;

    @Mock
    private MockHttpServletRequest request;

    @Mock
    private ModelAndViewContainer mavContainer;

    @Mock
    private WebDataBinderFactory binderFactory;

    @InjectMocks
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @BeforeEach
    void setUp() {
        loginMemberArgumentResolver = new LoginMemberArgumentResolver();
    }

    @Test
    @DisplayName("parameter가 Login annotation을 가지고 있고 Member 타입이면 true를 반환한다.")
    void supportsParameter() {
        given(parameter.hasParameterAnnotation(Login.class)).willReturn(true);
        willReturn(Long.class).given(parameter).getParameterType();

        boolean expect = loginMemberArgumentResolver.supportsParameter(parameter);

        then(expect).isTrue();
    }

    @Test
    @DisplayName("parameter가 Login annotation을 가지고 있지 않으면 Long 타입이면 false를 반환한다.")
    void supportsParameter_return_false_with_not_has_login_parameter() {
        given(parameter.hasParameterAnnotation(Login.class)).willReturn(false);
        willReturn(Long.class).given(parameter).getParameterType();

        boolean expect = loginMemberArgumentResolver.supportsParameter(parameter);

        then(expect).isFalse();
    }

    @Test
    @DisplayName("parameter가 Long 타입이 아니면 false를 반환한다.")
    void supportsParameter_return_false_with_not_member_type_class() {
        given(parameter.hasParameterAnnotation(Login.class)).willReturn(true);
        willReturn(Object.class).given(parameter).getParameterType();

        boolean expect = loginMemberArgumentResolver.supportsParameter(parameter);

        then(expect).isFalse();
    }

    @Test
    @DisplayName("NativeWebRequest을 HttpServletRequest으로 변환 후 속성으로 받아온 회원 식별 번호를 반환한다.")
    void resolveArgument() throws Exception {
        given(webRequest.getNativeRequest()).willReturn(request);
        given(request.getAttribute(anyString())).willReturn(1L);

        loginMemberArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        Long attributedMemberId = (Long) request.getAttribute(LOGIN_MEMBER);

        then(attributedMemberId).isEqualTo(1L);
    }
}
