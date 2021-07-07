package com.project.kodesalon.model.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.config.JacksonConfiguration;
import com.project.kodesalon.model.board.service.BoardService;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static com.project.kodesalon.common.ErrorCode.NOT_AUTHORIZED_MEMBER;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentRequest;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
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
    @DisplayName("게시물 식별번호를 인자로 받아 게시물을 삭제하고 HTTP 200을 반환한다.")
    void delete_success() throws Exception {
        mockMvc.perform(delete("/api/v1/boards/{boardId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/delete/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("boardId").description("삭제하려는 게시물 식별 번호"))));
    }

    @Test
    @DisplayName("권한을 가지지 않은 회원이 삭제를 시도할 경우, HTTP 400과 예외 코드를 반환한다.")
    void delete_fail_with_invalid_authorization() throws Exception {
        willThrow(new IllegalArgumentException(NOT_AUTHORIZED_MEMBER))
                .given(boardService)
                .delete(any(), anyLong());

        mockMvc.perform(delete("/api/v1/boards/{boardId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
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
                .delete(any(), anyLong());

        mockMvc.perform(delete("/api/v1/boards/{boardId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ALREADY_DELETED_BOARD))
                .andDo(document("board/delete/fail/already-deleted",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("이미 삭제된 게시물에 재삭제 요청에 대한 예외 코드")
                        )));
    }
}
