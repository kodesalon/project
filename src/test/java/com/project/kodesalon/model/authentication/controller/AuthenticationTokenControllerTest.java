package com.project.kodesalon.model.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.model.authentication.dto.JwtResponse;
import com.project.kodesalon.model.authentication.service.AuthenticationTokenService;
import com.project.kodesalon.model.member.service.dto.LoginRequest;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.NoSuchElementException;

import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentRequest;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class AuthenticationTokenControllerTest {
    private final LoginRequest loginRequest = new LoginRequest("alias", "Password123!!");
    private final LoginResponse loginResponse = new LoginResponse("access token", "refresh token", 1L, "alias");
    private final JwtResponse jwtResponse = new JwtResponse("access token", "refresh token");

    private MockMvc mockMvc;

    @InjectMocks
    private AuthenticationTokenController authenticationTokenController;

    @Mock
    private AuthenticationTokenService authenticationTokenService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationTokenController)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("로그인 성공하면 회원 식별자, 별명을 담은 DTO을 Http 200으로 응답합니다.")
    void login_success() throws Exception {
        given(authenticationTokenService.login(any(LoginRequest.class))).willReturn(loginResponse);

        this.mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andDo(document("login/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("로그인 할 alias"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 할 패스워드")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("jwt access token"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("jwt refresh token"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("member 식별자"),
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("member alias"))));
    }

    @Test
    @DisplayName("로그인 시 비밀번호 틀렸을 경우, 예외 메세지를 담은 DTO을 Http 400으로 응답합니다.")
    void login_fail_with_invalid_password() throws Exception {
        given(authenticationTokenService.login(any(LoginRequest.class)))
                .willThrow(new IllegalArgumentException("비밀 번호가 일치하지 않습니다."));

        this.mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀 번호가 일치하지 않습니다."))
                .andDo(document("login/fail/mismatch_password",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("로그인 할 alias"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 할 password")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("비밀번호 불일치 에러 메세지"))));
    }

    @Test
    @DisplayName("로그인 시 존재하지 않는 아이디(Alias)일 경우, 예외 메세지를 담은 DTO을 Http 400으로 응답합니다.")
    void login_fail_with_invalid_alias() throws Exception {
        given(authenticationTokenService.login(any(LoginRequest.class)))
                .willThrow(new NoSuchElementException("존재하는 아이디를 입력해주세요."));

        this.mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("존재하는 아이디를 입력해주세요."))
                .andDo(document("login/fail/no_alias",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("존재하지 않는 아이디(Alias) 예러 메세지"))));
    }
}
