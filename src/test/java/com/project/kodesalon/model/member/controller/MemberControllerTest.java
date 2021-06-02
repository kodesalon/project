package com.project.kodesalon.model.member.controller;

import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.model.member.dto.CreateMemberRequestDto;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.dto.SelectMemberResponseDto;
import com.project.kodesalon.model.member.exception.UnAuthorizedException;
import com.project.kodesalon.model.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
    private final String createRequestJson = "{\"alias\" : \"alias\", \"password\" : \"Password123!!\", " +
            "\"name\" : \"이름\", \"email\" : \"email@email.com\", \"phone\" : \"010-1111-2222\"}";
    private final String joinUrl = "/api/v1/members";
    private final String selectUrl = "/api/v1/members/{memberId}";

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
        given(memberService.login(any(LoginRequestDto.class)))
                .willReturn(new LoginResponseDto(1L, "alias"));

        this.mockMvc.perform(
                post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"memberId\":1,\"alias\":\"alias\"}"))
                .andDo(document("login/success",
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
                .willThrow(new UnAuthorizedException("일치하는 비밀번호를 입력해주세요."));

        this.mockMvc.perform(
                post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("{\"message\":\"일치하는 비밀번호를 입력해주세요.\"}"))
                .andDo(document("login/fail/mismatch_password",
                        responseFields(
                                fieldWithPath("message").description("예외 메세지"))));
    }

    @Test
    @DisplayName("존재하지 않는 사용자는 401 Status와 에러 메세지를 Response 합니다.")
    void not_exisit_alias_response_failed_message() throws Exception {
        given(memberService.login(any(LoginRequestDto.class)))
                .willThrow(new UnAuthorizedException("존재하는 Alias를 입력해주세요."));

        this.mockMvc.perform(
                post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("{\"message\":\"존재하는 Alias를 입력해주세요.\"}"))
                .andDo(document("login/fail/no_alias",
                        responseFields(
                                fieldWithPath("message").description("예외 메세지")
                        )));
    }

    @Test
    @DisplayName("사용자가 존재하지 않는다면 회원가입을 진행하고 201 상태를 response합니다.")
    void create_member_response_success() throws Exception {
        given(memberService.join(any(CreateMemberRequestDto.class)))
                .willReturn(new LoginResponseDto(1L, "alias"));

        this.mockMvc.perform(
                post(joinUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"memberId\":1,\"alias\":\"alias\"}"))
                .andDo(document("join/success",
                        requestFields(
                                fieldWithPath("alias").description("회원 가입할 member의 alias"),
                                fieldWithPath("password").description("회원 가입할 member의 password"),
                                fieldWithPath("name").description("회원 가입할 member의 이름"),
                                fieldWithPath("email").description("회원 가입할 member의 email"),
                                fieldWithPath("phone").description("회원 가입할 member의 phone")
                        ), responseFields(
                                fieldWithPath("memberId").description("회원 가입한 member의 식별자"),
                                fieldWithPath("alias").description("회원 가입한 member의 alias")
                        )));
    }

    @Test
    @DisplayName("이미 존재하는 회원이면 409 상태를 response합니다.")
    void existing_alias_response_fail() throws Exception {
        given(memberService.join(any(CreateMemberRequestDto.class)))
                .willThrow(new IllegalStateException("이미 존재하는 아이디입니다"));

        this.mockMvc.perform(
                post(joinUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequestJson))
                .andExpect(status().isConflict())
                .andExpect(content().string("{\"message\":\"이미 존재하는 아이디입니다\"}"))
                .andDo(document("join/fail/existing_alias",
                        responseFields(
                                fieldWithPath("message").description("이미 존재하는 회원 에러 메세지")
                        )));
    }

    @Test
    @DisplayName("아이디가 형식에 맞지 않으면 403 상태를 response합니다.")
    void invalid_alias_response_fail() throws Exception {
        this.mockMvc.perform(
                post(joinUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"alias\" : \"\", \"password\" : \"Password123!!\", \"name\" : \"이름\", " +
                                "\"email\" : \"email@email.com\", \"phone\" : \"010-1111-2222\"}"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"message\":\"아이디는 영문으로 시작해야 하며 4자리 이상 15자리 이하의 영문 혹은 숫자가 포함되어야 합니다.\"}"))
                .andDo(document("join/fail/invalid_alias",
                        responseFields(
                                fieldWithPath("message").description("유효하지 않은 Alias 에러 메세지")
                        )));
    }

    @Test
    @DisplayName("비밀번호가 형식에 맞지 않으면 403 상태를 response합니다.")
    void invalid_password_response_fail() throws Exception {
        this.mockMvc.perform(
                post(joinUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"alias\" : \"alias\", \"password\" : \"\", \"name\" : \"이름\", " +
                                "\"email\" : \"email@email.com\", \"phone\" : \"010-1111-2222\"}"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"message\":\"비밀번호는 영어 소문자, 대문자, 숫자, 특수문자를 포함한 8자리이상 16자리 이하여야 합니다.\"}"))
                .andDo(document("join/fail/invalid_password",
                        responseFields(
                                fieldWithPath("message").description("유효하지 않은 Password 에러 메세지")
                        )));
    }

    @Test
    @DisplayName("이름이 형식에 맞지 않으면 403 상태를 response합니다.")
    void invalid_name_response_fail() throws Exception {
        this.mockMvc.perform(
                post(joinUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"alias\" : \"alias\", \"password\" : \"Password123!!\", \"name\" : \"\", " +
                                "\"email\" : \"email@email.com\", \"phone\" : \"010-1111-2222\"}"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"message\":\"이름은 2자리 이상 17자리 이하의 한글이어야 합니다.\"}"))
                .andDo(document("join/fail/invalid_name",
                        responseFields(
                                fieldWithPath("message").description("유효하지 않은 Name 에러 메세지")
                        )));
    }

    @Test
    @DisplayName("이메일이 형식에 맞지 않으면 403 상태를 response합니다.")
    void invalid_email_response_fail() throws Exception {
        this.mockMvc.perform(
                post(joinUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"alias\" : \"alias\", \"password\" : \"Password123!!\", \"name\" : \"이름\", " +
                                "\"email\" : \"emailemail.com\", \"phone\" : \"010-1111-2222\"}"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"message\":\"이메일은 이메일주소@회사.com 형식 이어야 합니다.\"}"))
                .andDo(document("join/fail/invalid_email",
                        responseFields(
                                fieldWithPath("message").description("유효하지 않은 Eamil 에러 메세지")
                        )));
    }

    @Test
    @DisplayName("핸드폰이 형식에 맞지 않으면 403 상태를 response합니다.")
    void invalid_phone_response_fail() throws Exception {
        this.mockMvc.perform(
                post(joinUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"alias\" : \"alias\", \"password\" : \"Password123!!\", \"name\" : \"이름\", " +
                                "\"email\" : \"email@email.com\", \"phone\" : \"01111-2222\"}"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"message\":\"핸드폰 번호는 [휴대폰 앞자리 번호]- 3자리 혹은 4자리 수 - 4자리수의 형식 이어야 합니다.\"}"))
                .andDo(document("join/fail/invalid_phone",
                        responseFields(
                                fieldWithPath("message").description("유효하지 않은 phone 에러 메세지")
                        )));
    }

    @Test
    @DisplayName("존재하닌 회원을 조회하면 200 상태를 response 합니다.")
    void select_exist_member_response_success() throws Exception {
        given(memberService.selectMember(anyLong()))
                .willReturn(new SelectMemberResponseDto("alias", "이름", "email@email.com", "010-1111-2222"));

        this.mockMvc.perform(get(selectUrl, "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"alias\":\"alias\",\"name\":\"이름\",\"email\":\"email@email.com\",\"phone\":\"010-1111-2222\"}"))
                .andDo(document("select/success",
                        responseFields(
                            fieldWithPath("alias").description("조회한 Alias"),
                            fieldWithPath("name").description("조회한 Name"),
                            fieldWithPath("email").description("조회한 Email"),
                            fieldWithPath("phone").description("조회한 Phone")
                        )));
    }
}
