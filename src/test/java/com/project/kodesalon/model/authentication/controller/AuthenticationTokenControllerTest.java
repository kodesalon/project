package com.project.kodesalon.model.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.model.authentication.service.AuthenticationTokenService;
import com.project.kodesalon.model.authentication.service.dto.JwtResponse;
import com.project.kodesalon.model.authentication.service.dto.LoginRequest;
import com.project.kodesalon.model.authentication.service.dto.LoginResponse;
import com.project.kodesalon.model.authentication.service.dto.TokenRefreshRequest;
import io.jsonwebtoken.JwtException;
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

import javax.persistence.EntityNotFoundException;

import static com.project.kodesalon.common.ErrorCode.INCORRECT_PASSWORD;
import static com.project.kodesalon.common.ErrorCode.INVALID_JWT_TOKEN;
import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_MEMBER_ALIAS;
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
    private final TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest("refresh token");
    private final JwtResponse jwtResponse = new JwtResponse("accessToken", "refreshToken");

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
    @DisplayName("로그인 성공하면 회원 식별 번호, 별명을 담은 DTO을 Http 200으로 응답합니다.")
    void login_success() throws Exception {
        given(authenticationTokenService.login(any(LoginRequest.class))).willReturn(loginResponse);

        this.mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andDo(document("auth/login/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("로그인 할 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 할 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("JWT access 토큰"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("refresh 토큰"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별 번호"),
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("회원 아이디"))));
    }

    @Test
    @DisplayName("로그인 시 비밀번호 틀렸을 경우, 예외 메세지를 담은 DTO을 Http 400으로 응답합니다.")
    void login_fail_with_invalid_password() throws Exception {
        given(authenticationTokenService.login(any(LoginRequest.class)))
                .willThrow(new IllegalArgumentException(INCORRECT_PASSWORD));

        this.mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INCORRECT_PASSWORD))
                .andDo(document("auth/login/fail/mismatch-password",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("로그인 할 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 할 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("비밀번호가 일치하지 않는 경우에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("로그인 시 존재하지 않는 아이디(Alias)일 경우, 예외 메세지를 담은 DTO을 Http 400으로 응답합니다.")
    void login_fail_with_invalid_alias() throws Exception {
        given(authenticationTokenService.login(any(LoginRequest.class)))
                .willThrow(new EntityNotFoundException(NOT_EXIST_MEMBER_ALIAS));

        this.mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(NOT_EXIST_MEMBER_ALIAS))
                .andDo(document("auth/login/fail/no-alias",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("존재하지 않는 아이디에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("유효한 refresh token일 경우, 새로 발급받은 access token, refresh token을 DTO에 당마 Http 200으로 응답한다.")
    void refresh_success() throws Exception {
        given(authenticationTokenService.reissueAccessAndRefreshToken(any(TokenRefreshRequest.class))).willReturn(jwtResponse);

        this.mockMvc.perform(post("/api/v1/auth/refreshtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRefreshRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
                .andDo(document("auth/refreshtoken/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("refresh 토큰")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("새로 발급받은 JWT access 토큰"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("새로 발급받은 refresh 토큰"))));
    }

    @Test
    @DisplayName("존재하지 않거나 만료된 refresh token일 경우, 예외 메시지를 담은 DTO를 Http 400으로 응답한다.")
    void refresh_fail_no_existing_or_expired_token() throws Exception {
        given(authenticationTokenService.reissueAccessAndRefreshToken(any(TokenRefreshRequest.class))).willThrow(new JwtException(INVALID_JWT_TOKEN));

        this.mockMvc.perform(post("/api/v1/auth/refreshtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRefreshRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("auth/refreshtoken/fail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("유효하지 않거나 만료된 refresh 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 refresh 토큰 에러 코드"))));
    }
}
