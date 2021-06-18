package com.project.kodesalon.model.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.member.service.dto.ChangePasswordRequest;
import com.project.kodesalon.model.member.service.dto.ChangePasswordResponse;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.service.dto.LoginRequest;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import com.project.kodesalon.model.member.service.dto.SelectMemberResponse;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class MemberControllerTest {
    private final LoginRequest loginRequest = new LoginRequest("alias", "Password123!!");
    private final LoginResponse loginResponse = new LoginResponse(1L, "alias");
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
    @DisplayName("로그인 성공하면 회원 식별자, 별명을 담은 DTO을 Http 200으로 응답합니다.")
    void login_success() throws Exception {
        given(memberService.login(any(LoginRequest.class))).willReturn(loginResponse);

        this.mockMvc.perform(post("/api/v1/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andDo(document("login/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("로그인 할 alias"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 할 패스워드")
                        ),
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("member 식별자"),
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("member alias"))));
    }

    @Test
    @DisplayName("로그인 시 비밀번호 틀렸을 경우, 예외 메세지를 담은 DTO을 Http 400으로 응답합니다.")
    void login_fail_with_invalid_password() throws Exception {
        given(memberService.login(any(LoginRequest.class)))
                .willThrow(new IllegalArgumentException("비밀 번호가 일치하지 않습니다."));

        this.mockMvc.perform(post("/api/v1/members/login")
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
        given(memberService.login(any(LoginRequest.class)))
                .willThrow(new NoSuchElementException("존재하는 아이디를 입력해주세요."));

        this.mockMvc.perform(post("/api/v1/members/login")
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

    @Test
    @DisplayName("회원가입이 성공하면 회원가입한 회원 식별자, 별명을 담은 DTO를 Http 200으로 응답합니다.")
    void join_success() throws Exception {
        given(memberService.join(any(CreateMemberRequest.class))).willReturn(loginResponse);

        this.mockMvc.perform(post("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(1))
                .andExpect(jsonPath("$.alias").value("alias"))
                .andDo(document("join/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("회원 가입할 member의 alias"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("회원 가입할 member의 password"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 가입할 member의 이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 가입할 member의 email"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("회원 가입할 member의 phone")
                        ),
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 가입한 member의 식별자"),
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("회원 가입한 member의 alias"))));
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 아이디(Alias)일 경우, 예외 메세지를 다음 DTO를 Http 400으로 응답합니다.")
    void join_fail_with_already_exist() throws Exception {
        given(memberService.join(any(CreateMemberRequest.class)))
                .willThrow(new IllegalStateException("이미 존재하는 아이디입니다"));

        this.mockMvc.perform(post("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 존재하는 아이디입니다"))
                .andDo(document("join/fail/existing_alias",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("이미 존재하는 아이디(Alias) 에러 메세지"))));
    }

    @Test
    @DisplayName("회원가입 시 유효하지 않은 아이디(Alias)를 입력할 경우, 예외 메시지를 다음 DTO를 Http 400으로 응답합니다.")
    void join_fail_with_invalid_alias() throws Exception {
        CreateMemberRequest createMemberRequestWithInvalidAlias
                = new CreateMemberRequest("", "Password123!!", "이름", "email@email.com", "010-1111-2222");

        this.mockMvc.perform(post("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequestWithInvalidAlias)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("아이디는 영문으로 시작해야 하며 4자리 이상 15자리 이하의 영문 혹은 숫자가 포함되어야 합니다."))
                .andDo(document("join/fail/invalid_alias",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("유효하지 않은 아이디(Alias) 에러 메세지"))));
    }

    @Test
    @DisplayName("회원가입 시 유효하지 않은 비밀번호를 입력할 경우, 예외 메시지를 다음 DTO를 Http 400으로 응답합니다.")
    void join_fail_with_invalid_password() throws Exception {
        CreateMemberRequest createMemberRequestWithInvalidPassword
                = new CreateMemberRequest("alias", "", "이름", "email@email.com", "010-1111-2222");

        this.mockMvc.perform(post("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequestWithInvalidPassword)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호는 영어 소문자, 대문자, 숫자, 특수문자를 포함한 8자리이상 16자리 이하여야 합니다."))
                .andDo(document("join/fail/invalid_password",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("유효하지 않은 비밀번호(Password) 에러 메세지"))));
    }

    @Test
    @DisplayName("회원가입 시 유효하지 않은 이름을 입력할 경우, 예외 메시지를 다음 DTO를 Http 400으로 응답합니다.")
    void join_fail_with_invalid_name() throws Exception {
        CreateMemberRequest createMemberRequestWithInvalidName
                = new CreateMemberRequest("alias", "Password123!!", "", "email@email.com", "010-1111-2222");

        this.mockMvc.perform(post("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequestWithInvalidName)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이름은 2자리 이상 17자리 이하의 한글이어야 합니다."))
                .andDo(document("join/fail/invalid_name",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("유효하지 않은 이름(Name) 에러 메세지"))));
    }

    @Test
    @DisplayName("회원가입 시 유효하지 않은 이메일을 입력할 경우, 예외 메시지를 다음 DTO를 Http 400으로 응답합니다.")
    void join_fail_with_invalid_email() throws Exception {
        CreateMemberRequest createMemberRequestWithInvalidEmail
                = new CreateMemberRequest("alias", "Password123!!", "이름", " ", "010-1111-2222");

        this.mockMvc.perform(post("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequestWithInvalidEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일은 id@domain.com과 같은 형식이어야 합니다."))
                .andDo(document("join/fail/invalid_email",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("유효하지 않은 이메일(Email) 에러 메세지"))));
    }

    @Test
    @DisplayName("회원가입 시 유효하지 않은 휴대폰 번호를 입력할 경우, 예외 메시지를 다음 DTO를 Http 400으로 응답합니다.")
    void join_fail_with_invalid_phone() throws Exception {
        CreateMemberRequest createMemberRequestWithInvalidPhone
                = new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", "");

        this.mockMvc.perform(post("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequestWithInvalidPhone)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("휴대폰 번호는 [3자리 수] - [3 ~ 4자리 수] - [4자리 수]의 형식 이어야 합니다."))
                .andDo(document("join/fail/invalid_phone",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("유효하지 않은 phone 에러 메세지"))));
    }

    @Test
    @DisplayName("존재하는 회원을 조회하면 200 상태를 response 합니다.")
    void select_exist_member_response_success() throws Exception {
        given(memberService.selectMember(anyLong()))
                .willReturn(new SelectMemberResponse("alias", "이름", "email@email.com", "010-1111-2222"));

        this.mockMvc.perform(get("/api/v1/members/{memberId}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alias").value("alias"))
                .andExpect(jsonPath("$.name").value("이름"))
                .andExpect(jsonPath("$.email").value("email@email.com"))
                .andExpect(jsonPath("$.phone").value("010-1111-2222"))
                .andDo(document("select/success",
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("memberId").description("조회할 회원의 식별자")
                        ),
                        responseFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("조회한 Alias"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("조회한 Name"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("조회한 Email"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("조회한 Phone"))));
    }

    @Test
    @DisplayName("존재하지 않는 회원을 조회하면 400 상태를 responses 합니다")
    void select_no_exist_member_response_fail() throws Exception {
        given(memberService.selectMember(anyLong()))
                .willThrow(new NoSuchElementException("찾으려는 회원이 없습니다"));

        this.mockMvc.perform(get("/api/v1/members/{memberId}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("찾으려는 회원이 없습니다"))
                .andDo(document("select/fail/no_member",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("존재하는 회원이 없을 때의 예외 메세지"))));
    }

    @Test
    @DisplayName("비밀번호 변경시, 변경하려는 비밀번호, 회원 식별 번호를 전달받아 비밀번호를 변경하고 200 상태 + 성공 메세지를 반환합니다.")
    public void changePassword() throws Exception {
        given(memberService.changePassword(anyLong(), any(ChangePasswordRequest.class)))
                .willReturn(new ChangePasswordResponse("비밀번호 변경 성공하였습니다."));

        this.mockMvc.perform(put("/api/v1/members/{memberId}", 1L)
                .content(objectMapper.writeValueAsString(changePasswordRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("비밀번호 변경 성공하였습니다."))
                .andDo(document("changePassword/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("memberId").description("회원 식별 번호")
                        ),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("변경하려는 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("비밀번호 변경 성공 메세지")
                        )));
    }

    @Test
    @DisplayName("비밀번호 변경시, 변경하려는 비밀변호가 유효하지 않은 경우 400 상태 + 예외 메세지를 반환합니다.")
    void failed_change_password_with_invalid_password() throws Exception {
        ChangePasswordRequest changePasswordRequestWithInvalidPassword = new ChangePasswordRequest("비밀번호는 영어 소문자, 대문자, 숫자, 특수문자를 포함한 8자리이상 16자리 이하여야 합니다.");

        this.mockMvc.perform(put(("/api/v1/members/{memberId}"), 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequestWithInvalidPassword)))
                .andExpect(jsonPath("$.message").value("비밀번호는 영어 소문자, 대문자, 숫자, 특수문자를 포함한 8자리이상 16자리 이하여야 합니다."))
                .andDo(document("changePassword/fail/invalidPassword",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("유효하지 않은 비밀번호에 대한 예외 메세지"))));
    }

    @Test
    @DisplayName("비밀번호 변경시, 변경하려는 회원 식별자가 없는 경우 400 상태 + 예외 메세지를 반환합니다.")
    void failed_change_password_with_member_id_not_exist() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password123!!");

        given(memberService.changePassword(anyLong(), any(ChangePasswordRequest.class)))
                .willThrow(new NoSuchElementException("찾으려는 회원이 없습니다"));

        this.mockMvc.perform(put("/api/v1/members/{memberId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(jsonPath("$.message").value("찾으려는 회원이 없습니다"))
                .andDo(document("changePassword/fail/noMember",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("존재하지 않는 회원에 대한 예외 메세지"))));
    }
}
