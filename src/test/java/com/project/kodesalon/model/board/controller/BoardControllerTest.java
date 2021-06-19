package com.project.kodesalon.model.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.config.JacksonConfiguration;
import com.project.kodesalon.model.board.service.BoardService;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardUpdateRequest;
import com.project.kodesalon.model.board.service.dto.BoardUpdateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

import static com.project.kodesalon.common.ErrorCode.BOARD_WRITER_IS_NULL;
import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_CONTENT;
import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_TITLE;
import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_BOARD;
import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_MEMBER;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentRequest;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JacksonConfiguration.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class BoardControllerTest {

    private final BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(1L, "update title", "update content");

    private MockMvc mockMvc;

    @InjectMocks
    private BoardController boardController;

    @Mock
    private BoardService boardService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .apply(documentationConfiguration(restDocumentation))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("회원 식별 번호, 제목, 내용, 생성 날짜를 json으로 전달받아 게시물을 생성하고 HTTP 200을 반환한다.")
    public void save() throws Exception {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest(1L, "게시물 제목", "게시물 내용", LocalDateTime.now());
        mockMvc.perform(post("/api/v1/boards")
                .content(objectMapper.writeValueAsString(boardCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/create/success",
                        getDocumentRequest(),
                        requestFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 식별 번호"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시물 내용"),
                                fieldWithPath("createdDateTime").type(JsonFieldType.STRING).description("게시물 작성 날짜")
                        ))
                );
    }

    @Test
    @DisplayName("제목이 존재하지 않을 경우 HTTP 400과 예외 코드를 반환한다.")
    public void save_fail_with_invalid_title() throws Exception {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest(1L, "", "게시물 내용", LocalDateTime.now());
        mockMvc.perform(post("/api/v1/boards")
                .content(objectMapper.writeValueAsString(boardCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("board/create/fail/invalid-title",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 제목 예외 코드")
                        )));
    }

    @Test
    @DisplayName("내용이 존재하지 않을 경우 HTTP 400과 예외 코드를 반환한다.")
    public void save_fail_with_invalid_content() throws Exception {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest(1L, "게시물 제목", "", LocalDateTime.now());
        mockMvc.perform(post("/api/v1/boards")
                .content(objectMapper.writeValueAsString(boardCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("board/create/fail/invalid-content",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 내용 예외 코드")
                        )));
    }

    @Test
    @DisplayName("회원 식별 번호, 수정할 게시물의 제목과 내용을 요청받아 성공시 200을 응답합니다")
    void update() throws Exception {
        given(boardService.updateBoard(anyLong(), any(BoardUpdateRequest.class)))
                .willReturn(new BoardUpdateResponse("게시물 정보가 변경되었습니다"));

        mockMvc.perform(put("/api/v1/boards/{boardId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시물 정보가 변경되었습니다"))
                .andDo(document("board/update/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("boardId").description("수정할 게시물 식별자")
                        ),
                        requestFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성 회원의 식별자"),
                                fieldWithPath("updatedTitle").type(JsonFieldType.STRING).description("수정할 제목"),
                                fieldWithPath("updatedContent").type(JsonFieldType.STRING).description("수정할 내용")),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("게시물 정보가 변경되었습니다"))));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("회원 식별자가 null인 경우 400 http 상태외 예외 코드를 응답합니다")
    public void update_throw_exception_with_null_member_id(Long nullMemberId) throws Exception {
        BoardUpdateRequest boardUpdateRequest
                = new BoardUpdateRequest(nullMemberId, "update title", "update content");

        mockMvc.perform(put("/api/v1/boards/{boardId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(boardUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(BOARD_WRITER_IS_NULL))
                .andDo(document("board/update/fail/null-writer",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("작성자 식별자가 Null일 경우의 예외 코드"))));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("제목이 존재하지 않을 경우 400 Http 상태와 예외 코드를 응답합니다")
    public void update_throw_exception_with_invalid_title(String nullAndEmptyTitle) throws Exception {
        BoardUpdateRequest boardUpdateRequest
                = new BoardUpdateRequest(1L, nullAndEmptyTitle, "update content");

        mockMvc.perform(put("/api/v1/boards/{boardId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_BOARD_TITLE))
                .andDo(document("board/update/fail/invalid-title",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("게시물 수정 시 유효하지 않은 제목에 대한 예외 코드"))));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("내용이 존재하지 않을 경우 400 Http 상태와 예외 코드를 응답합니다")
    public void update_throw_exception_with_invalid_content(String nullAndEmptyContent) throws Exception {
        BoardUpdateRequest boardUpdateRequest
                = new BoardUpdateRequest(1L, "update content", nullAndEmptyContent);

        mockMvc.perform(put("/api/v1/boards/{boardId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(boardUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_BOARD_CONTENT))
                .andDo(document("board/update/fail/invalid-content",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("게시물 수정 시 유효하지 않은 내용에 대한 예외 코드"))));
    }
    @Test
    @DisplayName("게시물 수정시 존재하지 않는 작성자는 400 상태와 에러 코드를 응답합니다")
    void update_throw_exception_with_not_exist_member_id() throws Exception {
        given(boardService.updateBoard(anyLong(), any(BoardUpdateRequest.class)))
                .willThrow(new EntityNotFoundException(NOT_EXIST_MEMBER));

        mockMvc.perform(put("/api/v1/boards/{boardId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(NOT_EXIST_MEMBER))
                .andDo(document("board/update/fail/no-member",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("존재하지 않는 작성자에 대한 예외 메세지"))));
    }

    @Test
    @DisplayName("게시물 수정시 존재하지 않는 게시물은 400 상태와 에러 코드를 응답합니다")
    void update_throw_exception_with_not_exist_board_id() throws Exception {
        given(boardService.updateBoard(anyLong(), any(BoardUpdateRequest.class)))
                .willThrow(new EntityNotFoundException(NOT_EXIST_BOARD));

        mockMvc.perform(put("/api/v1/boards/{boardId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(NOT_EXIST_BOARD))
                .andDo(document("board/update/fail/no-board",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("존재하지 않는 게시물에 대한 예외 메세지"))));
    }
}
