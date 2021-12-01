package com.project.kodesalon.controller.member;

import com.project.kodesalon.config.AbstractControllerTest;
import com.project.kodesalon.service.dto.request.LoginRequest;
import com.project.kodesalon.service.dto.request.MemberChangePasswordRequest;
import com.project.kodesalon.service.dto.request.MemberCreateRequest;
import com.project.kodesalon.service.dto.request.MemberDeleteRequest;
import com.project.kodesalon.service.dto.response.BoardImageResponse;
import com.project.kodesalon.service.dto.response.BoardSelectResponse;
import com.project.kodesalon.service.dto.response.MemberSelectResponse;
import com.project.kodesalon.service.dto.response.MultiBoardSelectResponse;
import com.project.kodesalon.service.member.MemberService;
import org.hibernate.SessionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.project.kodesalon.exception.ErrorCode.ALREADY_EXIST_MEMBER_ALIAS;
import static com.project.kodesalon.exception.ErrorCode.DUPLICATED_PASSWORD;
import static com.project.kodesalon.exception.ErrorCode.INCORRECT_PASSWORD;
import static com.project.kodesalon.exception.ErrorCode.INVALID_DATE_TIME;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_ALIAS;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_EMAIL;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_NAME;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_PASSWORD;
import static com.project.kodesalon.exception.ErrorCode.INVALID_SESSION;
import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_MEMBER;
import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_MEMBER_ALIAS;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentRequest;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends AbstractControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @Override
    protected Object setController() {
        return memberController;
    }

    @Test
    @DisplayName("회원가입이 성공하면 Http 200으로 응답합니다.")
    void join_success_with_phone_null() throws Exception {
        MemberCreateRequest memberCreateRequest =
                new MemberCreateRequest("alias", "Password123!!", "이름", "email@email.com", null, LocalDateTime.now());

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberCreateRequest)))
                .andExpect(status().isOk())
                .andDo(document("member/join/success-phone-null",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("회원 가입할 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("회원 가입할 비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 가입할 이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 가입할 이메일"),
                                fieldWithPath("phone").optional().type(JsonFieldType.STRING).description("회원 가입할 핸드폰 번호"),
                                fieldWithPath("createdDateTime").type(JsonFieldType.STRING).description("회원 가입한 시간")
                        )));
    }

    @Test
    @DisplayName("회원가입이 성공하면 Http 200으로 응답합니다.")
    void join_success_with_valid_phone() throws Exception {
        MemberCreateRequest memberCreateRequest =
                new MemberCreateRequest("alias", "Password123!!", "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberCreateRequest)))
                .andExpect(status().isOk())
                .andDo(document("member/join/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("회원 가입할 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("회원 가입할 비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 가입할 이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 가입할 이메일"),
                                fieldWithPath("phone").optional().type(JsonFieldType.STRING).description("회원 가입할 핸드폰 번호"),
                                fieldWithPath("createdDateTime").type(JsonFieldType.STRING).description("회원 가입한 시간")
                        )));
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 아이디(Alias)일 경우, 예외 코드를 다음 DTO를 Http 400으로 응답합니다.")
    void join_fail_with_already_exist() throws Exception {
        MemberCreateRequest memberCreateRequest =
                new MemberCreateRequest("alias", "Password123!!", "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());
        willThrow(new IllegalStateException(ALREADY_EXIST_MEMBER_ALIAS))
                .given(memberService)
                .join(any(MemberCreateRequest.class));

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberCreateRequest)))
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
        MemberCreateRequest memberCreateRequestWithInvalidAlias =
                new MemberCreateRequest("", "Password123!!", "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberCreateRequestWithInvalidAlias)))
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
        MemberCreateRequest memberCreateRequestWithInvalidPassword =
                new MemberCreateRequest("alias", "", "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberCreateRequestWithInvalidPassword)))
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
        MemberCreateRequest memberCreateRequestWithInvalidName =
                new MemberCreateRequest("alias", "Password123!!", "", "email@email.com", "010-1234-5678", LocalDateTime.now());

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberCreateRequestWithInvalidName)))
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
        MemberCreateRequest memberCreateRequestWithInvalidEmail
                = new MemberCreateRequest("alias", "Password123!!", "이름", " ", "010-1234-5678", LocalDateTime.now());

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberCreateRequestWithInvalidEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_MEMBER_EMAIL))
                .andDo(document("member/join/fail/invalid-email",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 이메일에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("회원 가입시 삭제된 회원일 경우 400 상태와 예외 메세지를 반환합니다")
    void join_fail_with_deleted_member_alias() throws Exception {
        MemberCreateRequest memberCreateRequest =
                new MemberCreateRequest("alias", "Password123!!", "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());
        willThrow(new DataIntegrityViolationException(ALREADY_EXIST_MEMBER_ALIAS))
                .given(memberService)
                .join(any(MemberCreateRequest.class));

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberCreateRequest)))
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
        MemberCreateRequest memberCreateRequestWithInvalidCreatedTime
                = new MemberCreateRequest("alias", "Password123!!", "이름", "email@email.com", "010-1234-5678", invalidCreateDateTime);

        mockMvc.perform(post("/api/v1/members/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberCreateRequestWithInvalidCreatedTime)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_DATE_TIME))
                .andDo(document("member/join/fail/null-created-date-time",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("회원 가입 시간이 없는 경우에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("로그인 성공하면 회원 별명을 담은 DTO를 Http 200으로 응답합니다.")
    void login() throws Exception {
        String alias = "alias";
        LoginRequest loginRequest = new LoginRequest(alias, "Password123!!");

        mockMvc.perform(post("/api/v1/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andDo(document("member/login/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description("로그인 할 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 할 비밀번호")
                        )));
    }

    @Test
    @DisplayName("로그인 시 비밀번호 틀렸을 경우, 예외 메세지를 담은 DTO를 Http 400으로 응답합니다.")
    void login_fail_with_invalid_password() throws Exception {
        LoginRequest loginRequest = new LoginRequest("alias", "Password123!!");
        willThrow(new IllegalArgumentException(INCORRECT_PASSWORD))
                .given(memberService)
                .login(any(LoginRequest.class), any(HttpSession.class));

        mockMvc.perform(post("/api/v1/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INCORRECT_PASSWORD))
                .andDo(document("member/login/fail/mismatch-password",
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
    @DisplayName("로그인 시 존재하지 않는 아이디(Alias)일 경우, 예외 메세지를 담은 DTO를 Http 400으로 응답합니다.")
    void login_fail_with_invalid_alias() throws Exception {
        LoginRequest loginRequest = new LoginRequest("alias", "Password123!!");
        willThrow(new EntityNotFoundException(NOT_EXIST_MEMBER_ALIAS))
                .given(memberService)
                .login(any(LoginRequest.class), any(HttpSession.class));

        mockMvc.perform(post("/api/v1/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(NOT_EXIST_MEMBER_ALIAS))
                .andDo(document("member/login/fail/no-alias",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("존재하지 않는 아이디에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("로그아웃 후, HTTP 200을 반환한다.")
    void logout() throws Exception {
        mockMvc.perform(post("/api/v1/members/logout"))
                .andExpect(status().isOk())
                .andDo(document("member/logout/success",
                        getDocumentResponse()));
    }

    @Test
    @DisplayName("Session이 존재하지 않거나, 회원정보가 포함되어 있지 않을 경우, 예외 메세지를 담은 DTO를 Http 400으로 응답합니다.")
    void logout_fail_invalid_session() throws Exception {
        given(loginInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any()))
                .willThrow(new SessionException(INVALID_SESSION));

        mockMvc.perform(post("/api/v1/members/logout"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_SESSION))
                .andDo(document("member/logout/fail/no-session",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 세션에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("회원 식별 번호, 마지막으로 조회한 게시물의 식별 번호, 한번에 조회할 게시물의 크기를 전달받아 회원이 올린 게시물을 조회 후, " +
            "(회원 아이디 + 회원 이름 + 회원 이메일 + 회원 핸드폰 번호) + (제목 + 내용 + 생성 시간 + 작성자 별명 + 게시물 이미지들의 식별 번호 + 게시물 이미지들의 URL)과 " +
            "마지막 게시물이 아니라면 마지막 게시물 여부를 거짓으로 담은 Dto객체를 Http 200로 반환한다.")
    void selectMyBoards() throws Exception {
        List<BoardImageResponse> boardImages = Collections.singletonList(new BoardImageResponse(1L, "localhost:8080/bucket/directory/image.jpeg"));
        BoardSelectResponse boardSelectResponse1 = new BoardSelectResponse(1L, "title", "content", LocalDateTime.now(), 1L, "alias", boardImages);
        BoardSelectResponse boardSelectResponse2 = new BoardSelectResponse(2L, "title", "content", LocalDateTime.now(), 1L, "alias", boardImages);
        List<BoardSelectResponse> content = Arrays.asList(boardSelectResponse1, boardSelectResponse2);
        MultiBoardSelectResponse multiBoardSelectResponse = new MultiBoardSelectResponse(content, 1);
        given(memberService.selectMember(anyLong(), anyLong(), anyInt()))
                .willReturn(new MemberSelectResponse("alias", "이름", "email@email.com", "010-1111-2222", multiBoardSelectResponse));

        mockMvc.perform(get("/api/v1/members")
                .param("lastBoardId", "1")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/select-member/success/not-last",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("lastBoardId").description("마지막으로 조회한 게시물 식별 번호"),
                                parameterWithName("size").description("한번에 조회할 게시물 크기")),
                        responseFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("name").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("email").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("boards.boards[].boardId").type(JsonFieldType.NUMBER).description("게시물 식별 번호"),
                                fieldWithPath("boards.boards[].title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("boards.boards[].content").type(JsonFieldType.STRING).description("게시물 내용"),
                                fieldWithPath("boards.boards[].createdDateTime").type(JsonFieldType.ARRAY).description("게시물 생성 시간"),
                                fieldWithPath("boards.boards[].writerId").type(JsonFieldType.NUMBER).description("게시물 작성자 식별 번호"),
                                fieldWithPath("boards.boards[].writerAlias").type(JsonFieldType.STRING).description("게시물 작성자 아이디"),
                                fieldWithPath("boards.boards[].boardImages[].imageId").type(JsonFieldType.NUMBER).description("게시물 이미지 식별 번호"),
                                fieldWithPath("boards.boards[].boardImages[].imageUrl").type(JsonFieldType.STRING).description("게시물 이미지 URL"),
                                fieldWithPath("boards.last").type(JsonFieldType.BOOLEAN).description("마지막 게시물 여부"))));
    }

    @Test
    @DisplayName("회원 식별 번호, 마지막으로 조회한 게시물의 식별 번호, 한번에 조회할 게시물의 크기를 전달받아 회원이 올린 게시물을 조회 후, " +
            "(회원 아이디 + 회원 이름 + 회원 이메일 + 회원 핸드폰 번호) + (제목 + 내용 + 생성 시간 + 작성자 별명 + 게시물 이미지들의 식별 번호 + 게시물 이미지들의 URL)과 " +
            "마지막 게시물이라면 마지막 게시물 여부를 참으로 담은 Dto객체를 Http 200로 반환한다.")
    void selectMyBoards_with_last_board() throws Exception {
        List<BoardImageResponse> boardImages = Collections.singletonList(new BoardImageResponse(1L, "localhost:8080/bucket/directory/image.jpeg"));
        List<BoardSelectResponse> content = new ArrayList<>(Collections.singletonList(new BoardSelectResponse(0L, "title", "content", LocalDateTime.now(), 1L, "alias", boardImages)));
        MultiBoardSelectResponse multiBoardSelectResponse = new MultiBoardSelectResponse(content, 10);
        given(memberService.selectMember(anyLong(), anyLong(), anyInt()))
                .willReturn(new MemberSelectResponse("alias", "이름", "email@email.com", "010-1111-2222", multiBoardSelectResponse));

        mockMvc.perform(get("/api/v1/members")
                .param("lastBoardId", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/select-member/success/last",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("lastBoardId").description("마지막으로 조회한 게시물 식별 번호"),
                                parameterWithName("size").description("한번에 조회할 게시물 크기")),
                        responseFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("name").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("email").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("boards.boards[].boardId").type(JsonFieldType.NUMBER).description("게시물 식별 번호"),
                                fieldWithPath("boards.boards[].title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("boards.boards[].content").type(JsonFieldType.STRING).description("게시물 내용"),
                                fieldWithPath("boards.boards[].createdDateTime").type(JsonFieldType.ARRAY).description("게시물 생성 시간"),
                                fieldWithPath("boards.boards[].writerId").type(JsonFieldType.NUMBER).description("게시물 작성자 식별 번호"),
                                fieldWithPath("boards.boards[].writerAlias").type(JsonFieldType.STRING).description("게시물 작성자 아이디"),
                                fieldWithPath("boards.boards[].boardImages[].imageId").type(JsonFieldType.NUMBER).description("게시물 이미지 식별 번호"),
                                fieldWithPath("boards.boards[].boardImages[].imageUrl").type(JsonFieldType.STRING).description("게시물 이미지 URL"),
                                fieldWithPath("boards.last").type(JsonFieldType.BOOLEAN).description("마지막 게시물 여부"))));
    }

    @Test
    @DisplayName("회원이 자신이 올린 게시물을 가장 처음으로 조회할 경우, 조회할 게시물의 크기만 입력으로 받아 가장 최근 게시물을 조회 후, " +
            "(회원 아이디 + 회원 이름 + 회원 이메일 + 회원 핸드폰 번호) + (제목 + 내용 + 생성 시간 + 작성자 별명 + 게시물 이미지들의 식별 번호 + 게시물 이미지들의 URL)과 " +
            "마지막 게시물이라면 마지막 게시물 여부를 참으로 담은 Dto객체와 Http 200을 반환한다.")
    void selectMyBoard() throws Exception {
        List<BoardImageResponse> boardImages = Collections.singletonList(new BoardImageResponse(1L, "localhost:8080/bucket/directory/image.jpeg"));
        List<BoardSelectResponse> content = new ArrayList<>(Collections.singletonList(new BoardSelectResponse(1L, "title", "content", LocalDateTime.now(), 1L, "alias", boardImages)));
        MultiBoardSelectResponse multiBoardSelectResponse = new MultiBoardSelectResponse(content, 10);
        given(memberService.selectMember(anyLong(), anyLong(), anyInt()))
                .willReturn(new MemberSelectResponse("alias", "이름", "email@email.com", "010-1111-2222", multiBoardSelectResponse));

        mockMvc.perform(get("/api/v1/members")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member/select-member/success/first",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("size").description("한번에 조회할 게시물 크기")),
                        responseFields(
                                fieldWithPath("alias").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("name").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("email").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("phone").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("boards.boards[].boardId").type(JsonFieldType.NUMBER).description("게시물 식별 번호"),
                                fieldWithPath("boards.boards[].title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("boards.boards[].content").type(JsonFieldType.STRING).description("게시물 내용"),
                                fieldWithPath("boards.boards[].createdDateTime").type(JsonFieldType.ARRAY).description("게시물 생성 시간"),
                                fieldWithPath("boards.boards[].writerId").type(JsonFieldType.NUMBER).description("게시물 작성자 식별 번호"),
                                fieldWithPath("boards.boards[].writerAlias").type(JsonFieldType.STRING).description("게시물 작성자 아이디"),
                                fieldWithPath("boards.boards[].boardImages[].imageId").type(JsonFieldType.NUMBER).description("게시물 이미지 식별 번호"),
                                fieldWithPath("boards.boards[].boardImages[].imageUrl").type(JsonFieldType.STRING).description("게시물 이미지 URL"),
                                fieldWithPath("boards.last").type(JsonFieldType.BOOLEAN).description("마지막 게시물 여부"))));
    }

    @Test
    @DisplayName("비밀번호 변경시, 변경하려는 비밀번호, 회원 식별 번호를 전달받아 비밀번호를 변경하고 200 상태를 반환한다.")
    void changePassword() throws Exception {
        MemberChangePasswordRequest memberChangePasswordRequest = new MemberChangePasswordRequest("ChangePassword1!", LocalDateTime.now());

        mockMvc.perform(put("/api/v1/members/password")
                .content(objectMapper.writeValueAsString(memberChangePasswordRequest))
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
        MemberChangePasswordRequest memberChangePasswordRequestWithInvalidPassword = new MemberChangePasswordRequest("비밀번호는 영어 소문자, 대문자, 숫자, 특수문자를 포함한 8자리이상 16자리 이하여야 합니다.", LocalDateTime.now());

        mockMvc.perform(put("/api/v1/members/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberChangePasswordRequestWithInvalidPassword)))
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
        MemberChangePasswordRequest memberChangePasswordRequest = new MemberChangePasswordRequest("Password123!!", LocalDateTime.now());
        willThrow(new EntityNotFoundException(NOT_EXIST_MEMBER))
                .given(memberService)
                .changePassword(anyLong(), any(MemberChangePasswordRequest.class));

        mockMvc.perform(put("/api/v1/members/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberChangePasswordRequest)))
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
        MemberChangePasswordRequest memberChangePasswordRequest = new MemberChangePasswordRequest("Password123!!", LocalDateTime.now());
        willThrow(new IllegalArgumentException(DUPLICATED_PASSWORD))
                .given(memberService)
                .changePassword(anyLong(), any(MemberChangePasswordRequest.class));

        mockMvc.perform(put("/api/v1/members/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberChangePasswordRequest)))
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
        MemberChangePasswordRequest memberChangePasswordRequest = new MemberChangePasswordRequest("Password123!!", invalidLastModifiedDateTime);

        mockMvc.perform(put("/api/v1/members/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberChangePasswordRequest)))
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
        MemberDeleteRequest memberDeleteRequest = new MemberDeleteRequest(LocalDateTime.now());

        mockMvc.perform(delete("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDeleteRequest)))
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
        MemberDeleteRequest memberDeleteRequest = new MemberDeleteRequest(invalidDeletedDateTime);

        mockMvc.perform(delete("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDeleteRequest)))
                .andExpect(status().isBadRequest())
                .andDo(document("member/delete/fail/null-deleted-date-time",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("회원 탈퇴 시간이 없는 경우에 대한 예외 코드"))));
    }
}
