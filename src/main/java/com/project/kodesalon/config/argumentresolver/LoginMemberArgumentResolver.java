package com.project.kodesalon.config.argumentresolver;

import com.project.kodesalon.config.argumentresolver.annotation.Login;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

import static com.project.kodesalon.config.interceptor.LoginInterceptor.LOGIN_MEMBER;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasMemberIdType = Long.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginAnnotation && hasMemberIdType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        return request.getAttribute(LOGIN_MEMBER);
    }
}
