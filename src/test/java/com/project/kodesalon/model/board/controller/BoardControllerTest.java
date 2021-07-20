package com.project.kodesalon.model.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.config.JacksonConfiguration;
import com.project.kodesalon.model.board.service.BoardService;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardDeleteRequest;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static com.project.kodesalon.common.ErrorCode.ALREADY_DELETED_BOARD;
import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_ID;
import static com.project.kodesalon.common.ErrorCode.INVALID_DATE_TIME;
import static com.project.kodesalon.common.ErrorCode.NOT_AUTHORIZED_MEMBER;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentRequest;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JacksonConfiguration.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class BoardControllerTest {
    private final BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(1L, LocalDateTime.now());
    private MockMvc mockMvc;

    @InjectMocks
    private BoardController boardController;

    @Mock
    private BoardService boardService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .apply(documentationConfiguration(restDocumentation))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("제목, 내용, 생성 날짜를 json으로 전달받아 게시물을 생성하고 HTTP 200을 반환한다.")
    void save_success() throws Exception {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest("게시물 제목", "게시물 내용", LocalDateTime.now());
        mockMvc.perform(post("/api/v1/boards")
                .content(objectMapper.writeValueAsString(boardCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/create/success",
                        getDocumentRequest(),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("생성할 게시물 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("생성할 게시물 내용"),
                                fieldWithPath("createdDateTime").type(JsonFieldType.STRING).description("생성할 게시물 작성 날짜")
                        ))
                );
    }

    @Test
    @DisplayName("제목이 존재하지 않을 경우 HTTP 400과 예외 코드를 반환한다.")
    void save_fail_with_invalid_title() throws Exception {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest("", "게시물 내용", LocalDateTime.now());
        mockMvc.perform(post("/api/v1/boards")
                .content(objectMapper.writeValueAsString(boardCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("board/create/fail/invalid-title",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 제목에 대한 예외 코드")
                        )));
    }

    @Test
    @DisplayName("내용이 존재하지 않을 경우 HTTP 400과 예외 코드를 반환한다.")
    void save_fail_with_invalid_content() throws Exception {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest("게시물 제목", "", LocalDateTime.now());
        mockMvc.perform(post("/api/v1/boards")
                .content(objectMapper.writeValueAsString(boardCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("board/create/fail/invalid-content",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 내용에 대한 예외 코드")
                        )));
    }

    @Test
    @DisplayName("생성 시간이 존재하지 않을 경우 HTTP 400과 예외 코드를 반환한다.")
    public void save_fail_with_invalid_created_date_time() throws Exception {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest("게시물 제목", "게시물 내용", null);
        mockMvc.perform(post("/api/v1/boards")
                .content(objectMapper.writeValueAsString(boardCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("board/create/fail/null-created-date-time",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("게시물 생성 시간이 없을 경우에 대한 예외 코드")
                        )));
    }

    @Test
    @DisplayName("게시물 번호, 삭제 시간을 인자로 받아 게시물을 삭제하고 HTTP 200을 반환한다.")
    void delete_success() throws Exception {
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(1L, LocalDateTime.now());

        mockMvc.perform(delete("/api/v1/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardDeleteRequest)))
                .andExpect(status().isOk())
                .andDo(document("board/delete/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("삭제하려는 게시물 번호"),
                                fieldWithPath("deletedDateTime").type(JsonFieldType.STRING).description("삭제 시간"))));
    }

    @Test
    @DisplayName("권한을 가지지 않은 회원이 삭제를 시도할 경우, HTTP 400과 예외 코드를 반환한다.")
    void delete_fail_with_invalid_authorization() throws Exception {
        willThrow(new IllegalArgumentException(NOT_AUTHORIZED_MEMBER))
                .given(boardService)
                .delete(any(), any(BoardDeleteRequest.class));

        mockMvc.perform(delete("/api/v1/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardDeleteRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(NOT_AUTHORIZED_MEMBER))
                .andDo(document("board/delete/fail/invalid-auth",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("권한이 없는 경우에 대한 예외 코드")
                        )));
    }

    @Test
    @DisplayName("이미 삭제된 게시물을 다시 삭제 시도할 경우, HTTP 400과 예외 코드를 반환한다.")
    void delete_fail_with_already_deleted() throws Exception {
        willThrow(new IllegalArgumentException(ALREADY_DELETED_BOARD))
                .given(boardService)
                .delete(any(), any(BoardDeleteRequest.class));

        mockMvc.perform(delete("/api/v1/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardDeleteRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ALREADY_DELETED_BOARD))
                .andDo(document("board/delete/fail/already-deleted",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("이미 삭제된 게시물에 재삭제 요청에 대한 예외 코드")
                        )));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("삭제하려는 게시물 번호가 null일 경우, HTTP 400과 예외 코드를 반환한다.")
    void delete_fail_with_null_board_id(Long boardId) throws Exception {
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(boardId, LocalDateTime.now());

        mockMvc.perform(delete("/api/v1/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardDeleteRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_BOARD_ID))
                .andDo(document("board/delete/fail/null-board-id",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 게시물 번호에 대한 예외 코드")
                        )));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("삭제 시간이 null일 경우, 예외가 발생환다.")
    void delete_fail_with_null_deleted_date_time(LocalDateTime deletedDateTime) throws Exception {
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(1L, deletedDateTime);

        mockMvc.perform(delete("/api/v1/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardDeleteRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_DATE_TIME))
                .andDo(document("board/delete/fail/null-deleted-date-time",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("유효하지 않은 삭제 시간에 대한 예외 코드")
                        )));
    }
}
