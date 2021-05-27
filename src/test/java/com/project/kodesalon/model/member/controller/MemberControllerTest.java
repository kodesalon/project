package com.project.kodesalon.model.member.controller;

import com.project.kodesalon.model.member.dto.CreateMemberRequestDto;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.exception.UnAuthorizedException;
import com.project.kodesalon.model.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class,
        SpringExtension.class, MockitoExtension.class})
@SpringBootTest
public class MemberControllerTest {
    private static final String LOGIN_REQUEST_JSON = "{\"alias\" : \"alias\", \"password\" : \"Password123!!\"}";
    private static final String NO_MEMBER_ELEMENT_EXCEPTION_MESSAGE = "존재하는 Alias를 입력해주세요.";
    private static final String PASSWORD_NOT_MATCH_EXCEPTION_MESSAGE = "일치하는 비밀번호를 입력해주세요.";
    private static final String LOGIN_URL = "/api/v1/members/login";
    private static final String CREATE_MEMBER_URL = "/api/v1/members";
    private static final String CREATE_MEMBER_JSON = "{\"alias\" : \"alias\", \"password\" : \"Password123!!\", " +
            "\"name\" : \"이름\", \"email\" : \"email@email.com\", \"phone\" : \"010-1111-2222\"}";
    private static final String INVALID_ALIAS_EXCEPTION = "Alias 는 영문으로 시작해야 하며 4자리 이상 15자리 이하의 영문 혹은 숫자가 포함되어야 합니다.";
    private static final String INVALID_PASSWORD_EXCEPTION = "Password는 영어 소문자, 대문자, 숫자, 특수문자를 포함한 8자리이상 16자리 이하여야 합니다.";
    private static final String INVALID_NAME_EXCEPTION = "Name은 2자리 이상 17자리 이하의 한글이어야 합니다.";
    private static final String INVALID_EMAIL_EXCEPTION = "Email은 이메일주소@회사.com 형식 이어야 합니다.";
    private static final String INVALID_PHONE_EXCEPTION = "핸드폰 번호는 [휴대폰 앞자리 번호]- 3자리 혹은 4자리 수 - 4자리수의 형식 이어야 합니다.";
    private static final String ALREADY_EXIST_MEMBER_EXCEPTION_MESSAGE = "이미 존재하는 Alias 입니다.";

    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("로그인이 성공하면 Alias, ID, Http Status 200를 Response 합니다.")
    void login_controller_return_success_response()
            throws Exception {
        when(memberService.login(any(LoginRequestDto.class)))
                .thenReturn(new ResponseEntity<>(new LoginResponseDto(1L, "alias"), HttpStatus.OK));

        this.mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(LOGIN_REQUEST_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
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
    void login_failed_response_failed_message()
            throws Exception {
        when(memberService.login(any(LoginRequestDto.class)))
                .thenThrow(new UnAuthorizedException(PASSWORD_NOT_MATCH_EXCEPTION_MESSAGE));

        this.mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(LOGIN_REQUEST_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(document("login_failed_mismatch_password",
                        requestFields(
                                fieldWithPath("alias").description("로그인 할 alias"),
                                fieldWithPath("password").description("로그인 할 password")
                        ), responseFields(
                                fieldWithPath("message").description("예외 메세지"))));
    }

    @Test
    @DisplayName("존재하지 않는 사용자는 401 Status와 에러 메세지를 Response 합니다.")
    void not_exisit_alias_response_failed_message()
            throws Exception {
        when(memberService.login(any(LoginRequestDto.class)))
                .thenThrow(new UnAuthorizedException(NO_MEMBER_ELEMENT_EXCEPTION_MESSAGE));

        this.mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(LOGIN_REQUEST_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(document("login_fail_no_alias",
                        requestFields(
                                fieldWithPath("alias").description("로그인 할 alias"),
                                fieldWithPath("password").description("로그인 할 password")
                        ), responseFields(
                                fieldWithPath("message").description("예외 메세지")
                        )));
    }
    
    @Test
    @DisplayName("회원 가입에 성공하면 201 Status와 Id, Alias를 response 합니다.")
    void join_member_response_success() throws Exception {
        when(memberService.joinMember(any(CreateMemberRequestDto.class)))
                .thenReturn(new ResponseEntity<>(new LoginResponseDto(1L, "alias"), HttpStatus.CREATED));
        
        this.mockMvc.perform(post(CREATE_MEMBER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_MEMBER_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("create_member_success",
                        requestFields(
                                fieldWithPath("alias").description("회원 가입 할 alias"),
                                fieldWithPath("password").description("회원 가입 할 password"),
                                fieldWithPath("name").description("회원 가입 할 name"),
                                fieldWithPath("email").description("회원 가입 할 email"),
                                fieldWithPath("phone").description("회원 가입 할 휴대전화 번호")
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("식별자"),
                                fieldWithPath("alias").description("회원 alias")
                        )));
    }

    @Test
    @DisplayName("이미 존재하는 Alias는 HttpStatus 409와 예외 메세지를 리턴합니다")
    void existing_alias_response_fail() throws Exception {
        when(memberService.joinMember(any(CreateMemberRequestDto.class)))
                .thenThrow(new IllegalStateException(ALREADY_EXIST_MEMBER_EXCEPTION_MESSAGE));

        this.mockMvc.perform(post(CREATE_MEMBER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_MEMBER_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(document("create_member_fail/existing_member",
                        responseFields(
                                fieldWithPath("message").description("예외 메세지")
                        )));
    }
}
