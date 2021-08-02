package com.project.kodesalon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.config.JacksonConfiguration;
import com.project.kodesalon.exception.GlobalExceptionHandler;
import com.project.kodesalon.service.BoardService;
import com.project.kodesalon.service.dto.request.BoardCreateRequest;
import com.project.kodesalon.service.dto.request.BoardDeleteRequest;
import com.project.kodesalon.service.dto.request.BoardUpdateRequest;
import com.project.kodesalon.service.dto.response.BoardSelectResponse;
import com.project.kodesalon.service.dto.response.MultiBoardSelectResponse;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.project.kodesalon.exception.ErrorCode.ALREADY_DELETED_BOARD;
import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_CONTENT;
import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_TITLE;
import static com.project.kodesalon.exception.ErrorCode.INVALID_DATE_TIME;
import static com.project.kodesalon.exception.ErrorCode.NOT_AUTHORIZED_MEMBER;
import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_BOARD;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentRequest;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JacksonConfiguration.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class BoardControllerTest {

    private final BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(LocalDateTime.now());
    private final BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest("update title", "update content", LocalDateTime.now());
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
    void save_fail_with_invalid_created_date_time() throws Exception {
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
    @DisplayName("삭제 시간을 인자로 받아 게시물을 삭제하고 HTTP 200을 반환한다.")
    void delete_success() throws Exception {
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(LocalDateTime.now());

        mockMvc.perform(delete("/api/v1/boards/{boardId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardDeleteRequest)))
                .andExpect(status().isOk())
                .andDo(document("board/delete/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("boardId").description("삭제하려는 게시물 번호")
                        ),
                        requestFields(
                                fieldWithPath("deletedDateTime").type(JsonFieldType.STRING).description("삭제 시간"))));
    }

    @Test
    @DisplayName("권한을 가지지 않은 회원이 삭제를 시도할 경우, HTTP 400과 예외 코드를 반환한다.")
    void delete_fail_with_invalid_authorization() throws Exception {
        willThrow(new IllegalArgumentException(NOT_AUTHORIZED_MEMBER))
                .given(boardService)
                .delete(any(), anyLong(), any(BoardDeleteRequest.class));

        mockMvc.perform(delete("/api/v1/boards/{boardId}", 1L)
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
                .delete(any(), anyLong(), any(BoardDeleteRequest.class));

        mockMvc.perform(delete("/api/v1/boards/{boardId}", 1L)
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

    @Test
    @DisplayName("존재하지 않는 게시물 삭제를 시도할 경우, HTTP 400과 예외 코드를 반환한다.")
    void delete_fail_with_not_exist_board() throws Exception {
        willThrow(new EntityNotFoundException(NOT_EXIST_BOARD))
                .given(boardService)
                .delete(any(), anyLong(), any(BoardDeleteRequest.class));

        mockMvc.perform(delete("/api/v1/boards/{boardId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardDeleteRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(NOT_EXIST_BOARD))
                .andDo(document("board/delete/fail/not-exist-board",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("존재하지 않는 게시물 삭제 요청에 대한 예외 코드")
                        )));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("삭제 시간이 null일 경우, 예외가 발생환다.")
    void delete_fail_with_null_deleted_date_time(LocalDateTime deletedDateTime) throws Exception {
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(deletedDateTime);

        mockMvc.perform(delete("/api/v1/boards/{boardId}", 1L)
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

    @Test
    @DisplayName("회원 식별 번호, 수정할 게시물의 제목과 내용을 요청받아 성공시 200을 응답합니다")
    void update() throws Exception {
        mockMvc.perform(put("/api/v1/boards/{boardId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(document("board/update/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("boardId").description("수정할 게시물 식별 번호")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정할 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 내용"),
                                fieldWithPath("lastModifiedDateTime").type(JsonFieldType.STRING).description("마지막으로 수정된 시간"))));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("제목이 존재하지 않을 경우 400 Http 상태와 예외 코드를 응답합니다")
    void update_throw_exception_with_invalid_title(String nullAndEmptyTitle) throws Exception {
        BoardUpdateRequest boardUpdateRequest
                = new BoardUpdateRequest(nullAndEmptyTitle, "update content", LocalDateTime.now());

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
    void update_throw_exception_with_invalid_content(String nullAndEmptyContent) throws Exception {
        BoardUpdateRequest boardUpdateRequest
                = new BoardUpdateRequest("update content", nullAndEmptyContent, LocalDateTime.now());

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

    @ParameterizedTest
    @NullSource
    @DisplayName("마지막으로 수정된 시간이 유효하지 않은 경우 예외 코드를 응답합니다.")
    void update_throw_exception_with_invalid_last_modified_date_time(LocalDateTime invalidLastModifiedDateTime) throws Exception {
        BoardUpdateRequest boardUpdateRequest
                = new BoardUpdateRequest("updated title", "updated content", invalidLastModifiedDateTime);

        mockMvc.perform(put("/api/v1/boards/{boardId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(INVALID_DATE_TIME))
                .andDo(document("board/update/fail/invalid-last-modified-date-time",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("게시물 수정 시 유효하지 않은 게시물 수정 시간에 대한 예외 코"))));
    }

    @Test
    @DisplayName("게시물 수정시 존재하지 않는 게시물은 400 상태와 에러 코드를 응답합니다")
    void update_throw_exception_with_not_exist_board_id() throws Exception {
        willThrow(new EntityNotFoundException(NOT_EXIST_BOARD))
                .given(boardService)
                .updateBoard(any(), anyLong(), any(BoardUpdateRequest.class));

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

    @Test
    @DisplayName("게시물 식별 번호를 전달받아 해당 게시물을 조회 후, (제목 + 내용 + 생성 시간 + 작성자 별명)을 담은 Dto객체를 Http 200로 반환한다.")
    void selectBoard() throws Exception {
        BoardSelectResponse boardSelectResponse = new BoardSelectResponse(1L, "게시물 제목", "게시물 내용", LocalDateTime.now(), 1L, "alias");
        given(boardService.selectBoard(anyLong())).willReturn(boardSelectResponse);

        mockMvc.perform(get("/api/v1/boards/{boardId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/select-single/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("boardId").description("게시물 식별 번호")
                        ),
                        responseFields(
                                fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시물 식별 번호"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시물 내용"),
                                fieldWithPath("createdDateTime").type(JsonFieldType.ARRAY).description("게시물 생성 시간"),
                                fieldWithPath("writerId").type(JsonFieldType.NUMBER).description("게시물 작성자 식별 번호"),
                                fieldWithPath("writerAlias").type(JsonFieldType.STRING).description("게시물 작성자 아이디")
                        )));
    }

    @Test
    @DisplayName("마지막으로 조회한 게시물의 식별 번호와 한번에 조회할 게시물의 크기를 전달받아 해당 게시물을 조회 후, (제목 + 내용 + 생성 시간 + 작성자 별명)과 마지막 게시물이 아니라면 마지막 게시물 여부를 거짓으로 담은 Dto객체를 Http 200로 반환한다.")
    void selectBoards() throws Exception {
        BoardSelectResponse boardSelectResponse1 = new BoardSelectResponse(1L, "title", "content", LocalDateTime.now(), 1L, "alias");
        BoardSelectResponse boardSelectResponse2 = new BoardSelectResponse(2L, "title", "content", LocalDateTime.now(), 1L, "alias");
        List<BoardSelectResponse> content = Arrays.asList(boardSelectResponse1, boardSelectResponse2);
        MultiBoardSelectResponse multiBoardSelectResponse = new MultiBoardSelectResponse(content, 1);
        given(boardService.selectBoards(anyLong(), anyInt())).willReturn(multiBoardSelectResponse);

        mockMvc.perform(get("/api/v1/boards")
                .param("lastBoardId", "1")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/select-multi/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("lastBoardId").description("마지막으로 조회한 게시물 식별 번호"),
                                parameterWithName("size").description("한번에 조회할 게시물 크기")
                        ),
                        responseFields(
                                fieldWithPath("boards[].boardId").type(JsonFieldType.NUMBER).description("게시물 식별 번호"),
                                fieldWithPath("boards[].title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("boards[].content").type(JsonFieldType.STRING).description("게시물 내용"),
                                fieldWithPath("boards[].createdDateTime").type(JsonFieldType.ARRAY).description("게시물 생성 시간"),
                                fieldWithPath("boards[].writerId").type(JsonFieldType.NUMBER).description("게시물 작성자 식별 번호"),
                                fieldWithPath("boards[].writerAlias").type(JsonFieldType.STRING).description("게시물 작성자 아이디"),
                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 게시물 여부")
                        )));
    }

    @Test
    @DisplayName("마지막으로 조회한 게시물의 식별 번호와 한번에 조회할 게시물의 크기를 전달받아 해당 게시물을 조회 후, (제목 + 내용 + 생성 시간 + 작성자 별명)과 마지막 게시물이라면 마지막 게시물 여부를 참으로 담은 Dto객체를 Http 200로 반환한다.")
    void selectBoards_with_last_board() throws Exception {
        List<BoardSelectResponse> content = new ArrayList<>(Collections.singletonList(new BoardSelectResponse(0L, "title", "content", LocalDateTime.now(), 1L, "alias")));
        MultiBoardSelectResponse multiBoardSelectResponse = new MultiBoardSelectResponse(content, 10);
        given(boardService.selectBoards(anyLong(), anyInt())).willReturn(multiBoardSelectResponse);

        mockMvc.perform(get("/api/v1/boards")
                .param("lastBoardId", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/select-multi-last/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("lastBoardId").description("마지막으로 조회한 게시물 식별 번호"),
                                parameterWithName("size").description("한번에 조회할 게시물 크기")
                        ),
                        responseFields(
                                fieldWithPath("boards[].boardId").type(JsonFieldType.NUMBER).description("게시물 식별 번호"),
                                fieldWithPath("boards[].title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("boards[].content").type(JsonFieldType.STRING).description("게시물 내용"),
                                fieldWithPath("boards[].createdDateTime").type(JsonFieldType.ARRAY).description("게시물 생성 시간"),
                                fieldWithPath("boards[].writerId").type(JsonFieldType.NUMBER).description("게시물 작성자 식별 번호"),
                                fieldWithPath("boards[].writerAlias").type(JsonFieldType.STRING).description("게시물 작성자 아이디"),
                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 게시물 여부")
                        )));
    }

    @Test
    @DisplayName("가장 처음으로 조회한 게시물의 경우, 조회할 게시물의 크기만 입력으로 받아 가장 최근 게시물을 조회 후, (제목 + 내용 + 생성 시간 + 작성자 별명)와 마지막 게시물이라면 마지막 게시물 여부를 참으로 담은 Dto객체를 Http 200로 반환한다.")
    void selectBoards_at_first() throws Exception {
        List<BoardSelectResponse> content = new ArrayList<>(Collections.singletonList(new BoardSelectResponse(1L, "title", "content", LocalDateTime.now(), 1L, "alias")));
        MultiBoardSelectResponse multiBoardSelectResponse = new MultiBoardSelectResponse(content, 10);
        given(boardService.selectBoards(any(), anyInt())).willReturn(multiBoardSelectResponse);

        mockMvc.perform(get("/api/v1/boards")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("board/select-multi-first/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("size").description("한번에 조회할 게시물 크기")
                        ),
                        responseFields(
                                fieldWithPath("boards[].boardId").type(JsonFieldType.NUMBER).description("게시물 식별 번호"),
                                fieldWithPath("boards[].title").type(JsonFieldType.STRING).description("게시물 제목"),
                                fieldWithPath("boards[].content").type(JsonFieldType.STRING).description("게시물 내용"),
                                fieldWithPath("boards[].createdDateTime").type(JsonFieldType.ARRAY).description("게시물 생성 시간"),
                                fieldWithPath("boards[].writerId").type(JsonFieldType.NUMBER).description("게시물 작성자 식별 번호"),
                                fieldWithPath("boards[].writerAlias").type(JsonFieldType.STRING).description("게시물 작성자 아이디"),
                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 게시물 여부")
                        )));
    }
}
