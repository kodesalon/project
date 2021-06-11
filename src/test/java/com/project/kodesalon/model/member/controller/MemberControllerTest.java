package com.project.kodesalon.model.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.model.member.controller.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.controller.dto.LoginRequest;
import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequestDto;
import com.project.kodesalon.model.member.service.dto.LoginRequestDto;
import com.project.kodesalon.model.member.service.dto.LoginResponseDto;
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
import org.springframework.restdocs.payload.JsonFieldType;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class MemberControllerTest {
    private final LoginRequest loginRequest = new LoginRequest("alias", "Password123!!");
    private final CreateMemberRequest createMemberRequest =
            new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", "010-1111-2222");

    private MockMvc mockMvc;

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    ObjectMapper objectMapper = new ObjectMapper();

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
                post("/api/v1/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andDo(document("login/success",
                        requestFields(
                                fieldWithPath("alias")
                                        .type(JsonFieldType.STRING)
                                        .description("로그인 할 alias"),
                                fieldWithPath("password")
                                        .type(JsonFieldType.STRING)
                                        .description("로그인 할 패스워드")
                        ),
                        responseFields(
                                fieldWithPath("memberId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("Member 식별자"),
                                fieldWithPath("alias")
                                        .type(JsonFieldType.STRING)
                                        .description("member alias"))));
    }

    @Test
    @DisplayName("비밀번호 실패시 400 Status와 예외 메세지를 Response합니다.")
    void login_failed_response_failed_message() throws Exception {
        given(memberService.login(any(LoginRequestDto.class)))
                .willThrow(HttpClientErrorException.create("비밀 번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST,
                        "", HttpHeaders.EMPTY, null, null));

        this.mockMvc.perform(post("/api/v1/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀 번호가 일치하지 않습니다."))
                .andDo(document("login/fail/mismatch_password",
                        requestFields(
                                fieldWithPath("alias")
                                        .type(JsonFieldType.STRING)
                                        .description("로그인 할 alias"),
                                fieldWithPath("password")
                                        .type(JsonFieldType.STRING)
                                        .description("로그인 할 password")
                        ), responseFields(
                                fieldWithPath("message")
                                        .type(JsonFieldType.STRING)
                                        .description("예외 메세지"))));
    }

    @Test
    @DisplayName("존재하지 않는 사용자는 400 Status와 에러 메세지를 Response 합니다.")
    void not_exist_alias_response_failed_message() throws Exception {
        given(memberService.login(any(LoginRequestDto.class)))
                .willThrow(HttpClientErrorException.create("존재하는 아이디를 입력해주세요.", HttpStatus.BAD_REQUEST,
                        "", HttpHeaders.EMPTY, null, null));

        this.mockMvc.perform(post("/api/v1/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("존재하는 아이디를 입력해주세요."))
                .andDo(document("login/fail/no_alias",
                        responseFields(
                                fieldWithPath("message")
                                        .type(JsonFieldType.STRING)
                                        .description("예외 메세지"))));
    }

    @Test
    @DisplayName("사용자가 존재하지 않는다면 회원가입을 진행하고 200 상태를 response합니다.")
    void create_member_response_success() throws Exception {
        given(memberService.join(any(CreateMemberRequestDto.class)))
                .willReturn(new LoginResponseDto(1L, "alias"));

        this.mockMvc.perform(
                post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andDo(document("join/success",
                        requestFields(
                                fieldWithPath("alias")
                                        .type(JsonFieldType.STRING)
                                        .description("회원 가입할 member의 alias"),
                                fieldWithPath("password")
                                        .type(JsonFieldType.STRING)
                                        .description("회원 가입할 member의 password"),
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("회원 가입할 member의 이름"),
                                fieldWithPath("email")
                                        .type(JsonFieldType.STRING)
                                        .description("회원 가입할 member의 email"),
                                fieldWithPath("phone")
                                        .type(JsonFieldType.STRING)
                                        .description("회원 가입할 member의 phone")
                        ), responseFields(
                                fieldWithPath("memberId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("회원 가입한 member의 식별자"),
                                fieldWithPath("alias")
                                        .type(JsonFieldType.STRING)
                                        .description("회원 가입한 member의 alias"))));
    }

    @Test
    @DisplayName("이미 존재하는 회원이면 400 상태를 response합니다.")
    void existing_alias_response_fail() throws Exception {
        given(memberService.join(any(CreateMemberRequestDto.class)))
                .willThrow(new IllegalStateException("이미 존재하는 아이디입니다"));

        this.mockMvc.perform(
                post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 존재하는 아이디입니다"))
                .andDo(document("join/fail/existing_alias",
                        responseFields(
                                fieldWithPath("message")
                                        .type(JsonFieldType.STRING)
                                        .description("이미 존재하는 회원 에러 메세지"))));
    }

    @Test
    @DisplayName("아이디가 형식에 맞지 않으면 400 상태를 response합니다.")
    void invalid_alias_response_fail() throws Exception {
        CreateMemberRequest invalidCreateMemberRequest = new CreateMemberRequest("", "Password123!!", "이름", "email@email.com", "010-1111-2222");

        this.mockMvc.perform(
                post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateMemberRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("아이디는 영문으로 시작해야 하며 4자리 이상 15자리 이하의 영문 혹은 숫자가 포함되어야 합니다."))
                .andDo(document("join/fail/invalid_alias",
                        responseFields(
                                fieldWithPath("message")
                                        .type(JsonFieldType.STRING)
                                        .description("유효하지 않은 Alias 에러 메세지"))));
    }

    @Test
    @DisplayName("비밀번호가 형식에 맞지 않으면 400 상태를 response합니다.")
    void invalid_password_response_fail() throws Exception {
        CreateMemberRequest invalidCreateMemberRequest = new CreateMemberRequest("alias", "", "이름", "email@email.com", "010-1111-2222");

        this.mockMvc.perform(
                post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateMemberRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호는 영어 소문자, 대문자, 숫자, 특수문자를 포함한 8자리이상 16자리 이하여야 합니다."))
                .andDo(document("join/fail/invalid_password",
                        responseFields(
                                fieldWithPath("message")
                                        .type(JsonFieldType.STRING)
                                        .description("유효하지 않은 Password 에러 메세지"))));
    }

    @Test
    @DisplayName("이름이 형식에 맞지 않으면 400 상태를 response합니다.")
    void invalid_name_response_fail() throws Exception {
        CreateMemberRequest invalidCreateMemberRequest = new CreateMemberRequest("alias", "Password123!!", "", "email@email.com", "010-1111-2222");

        this.mockMvc.perform(
                post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateMemberRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이름은 2자리 이상 17자리 이하의 한글이어야 합니다."))
                .andDo(document("join/fail/invalid_name",
                        responseFields(
                                fieldWithPath("message")
                                        .type(JsonFieldType.STRING)
                                        .description("유효하지 않은 Name 에러 메세지"))));
    }

    @Test
    @DisplayName("이메일이 형식에 맞지 않으면 400 상태를 response합니다.")
    void invalid_email_response_fail() throws Exception {
        CreateMemberRequest invalidCreateMemberRequest = new CreateMemberRequest("alias", "Password123!!", "이름", "", "010-1111-2222");

        this.mockMvc.perform(
                post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateMemberRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일은 이메일주소@회사.com 형식 이어야 합니다."))
                .andDo(document("join/fail/invalid_email",
                        responseFields(
                                fieldWithPath("message")
                                        .type(JsonFieldType.STRING)
                                        .description("유효하지 않은 Email 에러 메세지"))));
    }

    @Test
    @DisplayName("핸드폰이 형식에 맞지 않으면 400 상태를 response합니다.")
    void invalid_phone_response_fail() throws Exception {
        CreateMemberRequest invalidCreateMemberRequest = new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", "");

        this.mockMvc.perform(
                post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCreateMemberRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("핸드폰 번호는 [휴대폰 앞자리 번호]- 3자리 혹은 4자리 수 - 4자리수의 형식 이어야 합니다."))
                .andDo(document("join/fail/invalid_phone",
                        responseFields(
                                fieldWithPath("message")
                                        .type(JsonFieldType.STRING)
                                        .description("유효하지 않은 phone 에러 메세지"))));
    }
}
