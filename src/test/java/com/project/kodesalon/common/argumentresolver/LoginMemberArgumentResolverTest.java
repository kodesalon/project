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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.project.kodesalon.common.interceptor.LoginInterceptor.LOGIN_MEMBER;
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
    private Member member;

    @Mock
    private ModelAndViewContainer mavContainer;

    @Mock
    private WebDataBinderFactory binderFactory;

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

        boolean expect = loginMemberArgumentResolver.supportsParameter(parameter);

        then(expect).isTrue();
    }

    @Test
    @DisplayName("parameter가 Login annotation을 가지고 있지 않으면 Member 타입이면 false를 반환한다.")
    void supportsParameter_return_false_with_not_has_login_parameter() {
        given(parameter.hasParameterAnnotation(Login.class)).willReturn(false);
        willReturn(Member.class).given(parameter).getParameterType();

        boolean expect = loginMemberArgumentResolver.supportsParameter(parameter);

        then(expect).isFalse();
    }

    @Test
    @DisplayName("parameter가 Member 타입이 아니면 false를 반환한다.")
    void supportsParameter_return_false_with_not_member_type_class() {
        given(parameter.hasParameterAnnotation(Login.class)).willReturn(true);
        willReturn(Object.class).given(parameter).getParameterType();

        boolean expect = loginMemberArgumentResolver.supportsParameter(parameter);

        then(expect).isFalse();
    }

    @Test
    @DisplayName("NativeWebRequest을 HttpServletRequest으로 변환 후 속성으로 받아온 Member 객체를 반환한다.")
    void resolveArgument() throws Exception {
        given(webRequest.getNativeRequest()).willReturn(request);
        given(request.getAttribute(anyString())).willReturn(member);

        loginMemberArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        Member attributedMember = (Member) request.getAttribute(LOGIN_MEMBER);

        then(attributedMember).isEqualTo(member);
    }
}
