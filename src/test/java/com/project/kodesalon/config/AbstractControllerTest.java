package com.project.kodesalon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.common.argumentresolver.LoginMemberArgumentResolver;
import com.project.kodesalon.common.interceptor.LoginInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@Import(JacksonConfiguration.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;

    @Mock
    protected LoginInterceptor loginInterceptor;

    @Mock
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    private void setUp(RestDocumentationContextProvider restDocumentation) throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(setController())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .addInterceptors(loginInterceptor)
                .setCustomArgumentResolvers(loginMemberArgumentResolver)
                .setControllerAdvice(new GlobalExceptionHandler())
                .apply(documentationConfiguration(restDocumentation))
                .build();

        given(loginInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any()))
                .willReturn(true);

        given(loginMemberArgumentResolver.supportsParameter(any(MethodParameter.class))).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(MethodParameter.class), any(ModelAndViewContainer.class),
                any(NativeWebRequest.class), any())).willReturn(1L);
    }

    protected abstract Object setController();
}
