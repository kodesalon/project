package com.project.kodesalon.model.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.common.interceptor.LoginInterceptor;
import com.project.kodesalon.config.JacksonConfiguration;
import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.member.service.dto.ChangePasswordRequest;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.service.dto.DeleteMemberRequest;
import com.project.kodesalon.model.member.service.dto.SelectMemberResponse;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static com.project.kodesalon.common.ErrorCode.ALREADY_EXIST_MEMBER_ALIAS;
import static com.project.kodesalon.common.ErrorCode.DUPLICATED_PASSWORD;
import static com.project.kodesalon.common.ErrorCode.EXPIRED_JWT_TOKEN;
import static com.project.kodesalon.common.ErrorCode.INVALID_DATE_TIME;
import static com.project.kodesalon.common.ErrorCode.INVALID_HEADER;
import static com.project.kodesalon.common.ErrorCode.INVALID_JWT_TOKEN;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_ALIAS;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_EMAIL;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_NAME;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_PASSWORD;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_PHONE;
import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_MEMBER;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentRequest;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JacksonConfiguration.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class MemberControllerTest {

    private final CreateMemberRequest createMemberRequest =
            new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", "010-1111-2222", LocalDateTime.now());
    private final ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("ChangePassword1!", LocalDateTime.now());

    private MockMvc mockMvc;

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @Mock
    LoginInterceptor loginInterceptor;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .addInterceptors(loginInterceptor)
                .apply(documentationConfiguration(restDocumentation))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        given(loginInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any()))
                .willReturn(true);
    }

    @Test
    @DisplayName("회원가입이 성공하면 Http 200으로 응답합니다.")
    void join_success() throws Exception {
        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andExpect(status().isOk())
                .andDo(document("member/join/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("회원 가입할 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("회원 가입할 비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 가입할 이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 가입할 이메일"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("회원 가입할 핸드폰 번호"),
                                fieldWithPath("createdDateTime").type(JsonFieldType.STRING).description("회원 가입한 시간")
                        )));
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 아이디(Alias)일 경우, 예외 코드를 다음 DTO를 Http 400으로 응답합니다.")
    void join_fail_with_already_exist() throws Exception {
        willThrow(new IllegalStateException(ALREADY_EXIST_MEMBER_ALIAS))
                .given(memberService)
                .join(any(CreateMemberRequest.class));

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ALREADY_EXIST_MEMBER_ALIAS))
                .andDo(document("member/join/fail/existing-alias",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("이미 존재하는 아이디에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("회원가입 시 유효하지 않은 아이디(Alias)를 입력할 경우, 예외 메시지를 다음 DTO를 Http 400으로 응답합니다.")
    void join_fail_with_invalid_alias() throws Exception {
        CreateMemberRequest createMemberRequestWithInvalidAlias
                = new CreateMemberRequest("", "Password123!!", "이름", "email@email.com", "010-1111-2222", LocalDateTime.now());

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequestWithInvalidAlias)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_MEMBER_ALIAS))
                .andDo(document("member/join/fail/invalid-alias",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 아이디에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("회원가입 시 유효하지 않은 비밀번호를 입력할 경우, 예외 메시지를 다음 DTO를 Http 400으로 응답합니다.")
    void join_fail_with_invalid_password() throws Exception {
        CreateMemberRequest createMemberRequestWithInvalidPassword
                = new CreateMemberRequest("alias", "", "이름", "email@email.com", "010-1111-2222", LocalDateTime.now());

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequestWithInvalidPassword)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_MEMBER_PASSWORD))
                .andDo(document("member/join/fail/invalid-password",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 비밀번호에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("회원가입 시 유효하지 않은 이름을 입력할 경우, 예외 메시지를 다음 DTO를 Http 400으로 응답합니다.")
    void join_fail_with_invalid_name() throws Exception {
        CreateMemberRequest createMemberRequestWithInvalidName
                = new CreateMemberRequest("alias", "Password123!!", "", "email@email.com", "010-1111-2222", LocalDateTime.now());

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequestWithInvalidName)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_MEMBER_NAME))
                .andDo(document("member/join/fail/invalid-name",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 이름에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("회원가입 시 유효하지 않은 이메일을 입력할 경우, 예외 메시지를 다음 DTO를 Http 400으로 응답합니다.")
    void join_fail_with_invalid_email() throws Exception {
        CreateMemberRequest createMemberRequestWithInvalidEmail
                = new CreateMemberRequest("alias", "Password123!!", "이름", " ", "010-1111-2222", LocalDateTime.now());

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequestWithInvalidEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_MEMBER_EMAIL))
                .andDo(document("member/join/fail/invalid-email",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 이메일에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("회원가입 시 유효하지 않은 휴대폰 번호를 입력할 경우, 예외 메시지를 다음 DTO를 Http 400으로 응답합니다.")
    void join_fail_with_invalid_phone() throws Exception {
        CreateMemberRequest createMemberRequestWithInvalidPhone
                = new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", "", LocalDateTime.now());

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequestWithInvalidPhone)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_MEMBER_PHONE))
                .andDo(document("member/join/fail/invalid-phone",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 핸드폰 번호에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("회원 가입시 삭제된 회원일 경우 400 상태와 예외 메세지를 반환합니다")
    void join_fail_with_deleted_member_alias() throws Exception {
        willThrow(new DataIntegrityViolationException(ALREADY_EXIST_MEMBER_ALIAS))
                .given(memberService)
                .join(any(CreateMemberRequest.class));

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ALREADY_EXIST_MEMBER_ALIAS))
                .andDo(document("member/join/fail/deleted-alias",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("이미 삭제된 회원 어아다에 대한 회원 가입 예외 코드"))));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("회원 가입시 회원 가입 시간이 없는 경우 예외 메세지를 반환합니다.")
    void join_fail_with_null_created_date_time(LocalDateTime invalidCreateDateTime) throws Exception {
        CreateMemberRequest createMemberRequest
                = new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", "010-1111-2222", invalidCreateDateTime);

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMemberRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_DATE_TIME))
                .andDo(document("member/join/fail/null-created-date-time",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("회원 가입 시간이 없는 경우에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("존재하는 회원을 조회하면 200 상태를 response 합니다.")
    void select_exist_member_response_success() throws Exception {
        given(memberService.selectMember(any()))
                .willReturn(new SelectMemberResponse("alias", "이름", "email@email.com", "010-1111-2222"));

        mockMvc.perform(get("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alias").value("alias"))
                .andExpect(jsonPath("$.name").value("이름"))
                .andExpect(jsonPath("$.email").value("email@email.com"))
                .andExpect(jsonPath("$.phone").value("010-1111-2222"))
                .andDo(document("member/select/success",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("조회한 회원의 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("조회한 회원의 이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("조회한 회원의 이메일"),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description("조회한 회원의 핸드폰 번호"))));
    }

    @Test
    @DisplayName("비밀번호 변경시, 변경하려는 비밀번호, 회원 식별 번호를 전달받아 비밀번호를 변경하고 200 상태를 반환한다.")
    void changePassword() throws Exception {
        mockMvc.perform(put("/api/v1/members/password")
                .content(objectMapper.writeValueAsString(changePasswordRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/change-password/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("변경하려는 비밀번호"),
                                fieldWithPath("lastModifiedDateTime").type(JsonFieldType.STRING).description("비밀번호 변경 시간")
                        )));
    }

    @Test
    @DisplayName("비밀번호 변경시, 변경하려는 비밀변호가 유효하지 않은 경우 400 상태 + 예외 코드를 반환합니다.")
    void failed_change_password_with_invalid_password() throws Exception {
        ChangePasswordRequest changePasswordRequestWithInvalidPassword = new ChangePasswordRequest("비밀번호는 영어 소문자, 대문자, 숫자, 특수문자를 포함한 8자리이상 16자리 이하여야 합니다.", LocalDateTime.now());

        mockMvc.perform(put("/api/v1/members/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequestWithInvalidPassword)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_MEMBER_PASSWORD))
                .andDo(document("member/change-password/fail/invalid-password",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 비밀번호에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("비밀번호 변경시, 변경하려는 회원 식별 번호가 없는 경우 400 상태 + 예외 코드를 반환합니다.")
    void failed_change_password_with_member_id_not_exist() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password123!!", LocalDateTime.now());
        willThrow(new EntityNotFoundException(NOT_EXIST_MEMBER))
                .given(memberService)
                .changePassword(any(), any(ChangePasswordRequest.class));

        mockMvc.perform(put("/api/v1/members/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(NOT_EXIST_MEMBER))
                .andDo(document("member/change-password/fail/no-member",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("존재하지 않는 회원에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("비밀번호 변경시, 변경하려는 비밀번호가 기존 비밀번호와 일치하는 경우 400 상태 + 예외 코드를 반환합니다")
    void failed_change_password_with_duplicate_password() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password123!!", LocalDateTime.now());
        willThrow(new IllegalArgumentException(DUPLICATED_PASSWORD))
                .given(memberService)
                .changePassword(any(), any(ChangePasswordRequest.class));

        mockMvc.perform(put("/api/v1/members/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(DUPLICATED_PASSWORD))
                .andDo(document("member/change-password/fail/password-duplicate",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("기존 비밀번호와 일치할 경우에 대한 예외 코드"))));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("비밀번호 변경시, 마지막으로 변경된 시간이 없을 경우, 400 상태 + 예외 코드를 반환합니다")
    void failed_change_password_with_invalid_last_modified_date_time(LocalDateTime invalidLastModifiedDateTime) throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("Password123!!", invalidLastModifiedDateTime);

        mockMvc.perform(put("/api/v1/members/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_DATE_TIME))
                .andDo(document("member/change-password/fail/null-last-modified-date-time",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("마지막으로 수정된 시간이 없을 경우에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("회원의 식별 번호를 전달받아 회원을 탈퇴하고 200 상태 + 성공 메세지를 반환합니다.")
    void deleteMember() throws Exception {
        DeleteMemberRequest deleteMemberRequest = new DeleteMemberRequest(LocalDateTime.now());

        mockMvc.perform(delete("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deleteMemberRequest)))
                .andExpect(status().isOk())
                .andDo(document("member/delete/success",
                        getDocumentRequest(),
                        requestFields(
                                fieldWithPath("deletedDateTime").type(JsonFieldType.STRING).description("회원 탈퇴 시간"))));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("회원 탈퇴 시간이 존재하지 않을 경우 400 상태 + 예외 코드를 반환합니다.")
    void deleteMember_throws_exception_with_null_deleted_date_time(LocalDateTime invalidDeletedDateTime) throws Exception {
        DeleteMemberRequest deleteMemberRequest = new DeleteMemberRequest(invalidDeletedDateTime);

        mockMvc.perform(delete("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deleteMemberRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("member/delete/fail/null-deleted-date-time",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("회원 탈퇴 시간이 없는 경우에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("토큰이 만료된 경우 400 상태 + 예외 코드를 반환한다.")
    void access_token_expired() throws Exception {
        given(loginInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any()))
                .willThrow(new JwtException(EXPIRED_JWT_TOKEN));

        mockMvc.perform(delete("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(EXPIRED_JWT_TOKEN))
                .andDo(document("member/jwt/expired",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").description("만료 JWT 토큰에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("토큰이 유효하지 않을 경우 400 상태 + 예외 코드를 반환한다.")
    void invalid_access_token() throws Exception {
        given(loginInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any()))
                .willThrow(new JwtException(INVALID_JWT_TOKEN));

        mockMvc.perform(delete("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_JWT_TOKEN))
                .andDo(document("member/jwt/invalid",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").description("유효하지 않은 JWT 토큰에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("authorization 안에 내용이 없을 경우 400상태 + 예외 코드를 반환한다.")
    void invalid_authorization_throw_exception() throws Exception {
        given(loginInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any()))
                .willThrow(new JwtException(INVALID_HEADER));

        mockMvc.perform(delete("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_HEADER))
                .andDo(document("member/jwt/invalid-header",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").description("Header에 Authorization 속성이 없을 경우에 대한 예외 코드"))));
    }
}
