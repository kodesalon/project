package com.project.kodesalon.model.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.model.authentication.service.AuthenticationTokenService;
import com.project.kodesalon.model.authentication.service.dto.JwtResponse;
import com.project.kodesalon.model.authentication.service.dto.TokenRefreshRequest;
import com.project.kodesalon.model.member.service.dto.LoginRequest;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
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

import static com.project.kodesalon.common.ErrorCode.INVALID_JWT_TOKEN;
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
                .andExpect(jsonPath("$.code").value("비밀 번호가 일치하지 않습니다."))
                .andDo(document("login/fail/mismatch_password",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("로그인 할 alias"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 할 password")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("비밀번호 불일치 에러 메세지"))));
    }

    @Test
    @DisplayName("로그인 시 존재하지 않는 아이디(Alias)일 경우, 예외 메세지를 담은 DTO을 Http 400으로 응답합니다.")
    void login_fail_with_invalid_alias() throws Exception {
        given(authenticationTokenService.login(any(LoginRequest.class)))
                .willThrow(new EntityNotFoundException("존재하는 아이디를 입력해주세요."));

        this.mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("존재하는 아이디를 입력해주세요."))
                .andDo(document("login/fail/no_alias",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("존재하지 않는 아이디(Alias) 예러 메세지"))));
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
                .andDo(document("refreshtoken/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("jwt refresh token")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("새로 발급받은 jwt access token"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("새로 발급받은 jwt refresh token"))));
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
                .andDo(document("refreshtoken/fail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("유효하지 않거나 만료된 jwt refresh token")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 refresh token 에러 코드"))));
    }
}
