package com.project.kodesalon.model.member.controller;

import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class MemberControllerTest {
    private final String loginRequestJson = "{\"alias\" : \"alias\", \"password\" : \"Password123!!\"}";
    private final String loginUrl = "/api/v1/members/login";

    private MockMvc mockMvc;

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("로그인이 성공하면 Alias, ID, Http Status 200를 Response 합니다.")
    void login_controller_return_success_response() throws Exception {
        LoginResponseDto loginResponseDto = new LoginResponseDto(1L, "alias");

        given(memberService.login(any(LoginRequestDto.class)))
                .willReturn(loginResponseDto);

        this.mockMvc.perform(
                post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"memberId\":1,\"alias\":\"alias\"}"))
                .andDo(document("login_success",
                        requestFields(
                                fieldWithPath("alias").description("로그인 할 alias"),
                                fieldWithPath("password").description("로그인 할 패스워드")
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("Member 식별자"),
                                fieldWithPath("alias").description("member alias"))));
    }

    @Test
    @DisplayName("비밀번호 실패시 401 Status와 예외 메세지를 Response합니다.")
    void login_failed_response_failed_message() throws Exception {
        given(memberService.login(any(LoginRequestDto.class)))
                .willThrow(HttpClientErrorException.create("비밀 번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED,
                        "", HttpHeaders.EMPTY, null, null));

        this.mockMvc.perform(post(loginUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("{\"message\":\"비밀 번호가 일치하지 않습니다.\"}"))
                .andDo(document("login_failed_mismatch_password",
                        requestFields(
                                fieldWithPath("alias").description("로그인 할 alias"),
                                fieldWithPath("password").description("로그인 할 password")
                        ), responseFields(
                                fieldWithPath("message").description("예외 메세지"))));
    }

    @Test
    @DisplayName("존재하지 않는 사용자는 401 Status와 에러 메세지를 Response 합니다.")
    void not_exist_alias_response_failed_message() throws Exception {
        given(memberService.login(any(LoginRequestDto.class)))
                .willThrow(HttpClientErrorException.create("존재하는 아이디를 입력해주세요.", HttpStatus.UNAUTHORIZED,
                        "", HttpHeaders.EMPTY, null, null));

        this.mockMvc.perform(post(loginUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("{\"message\":\"존재하는 아이디를 입력해주세요.\"}"))
                .andDo(document("login_fail_no_alias",
                        requestFields(
                                fieldWithPath("alias").description("로그인 할 alias"),
                                fieldWithPath("password").description("로그인 할 password")
                        ), responseFields(
                                fieldWithPath("message").description("예외 메세지")
                        )));
    }
}
