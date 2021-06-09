package com.project.kodesalon.model.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.config.JacksonConfiguration;
import com.project.kodesalon.model.board.controller.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.BoardService;
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

import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentRequest;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(boardController)
                .apply(documentationConfiguration(restDocumentation))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("회원 식별 번호, 제목, 내용, 생성 날짜를 json으로 전달받아 게시물을 생성하고 HTTP status 201을 반환한다.")
    public void save() throws Exception {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest(1L, "게시물 제목", "게시물 내용", LocalDateTime.now());
        mockMvc.perform(post("/api/v1/boards")
                .content(objectMapper.writeValueAsString(boardCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
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
    @DisplayName("제목이 존재하지 않을 경우 HTTP status 400과 예외 메세지를 반환한다.")
    public void save_fail_invalid_title() throws Exception {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest(1L, "", "게시물 내용", LocalDateTime.now());
        mockMvc.perform(post("/api/v1/boards")
                .content(objectMapper.writeValueAsString(boardCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("board/create/fail/invalid-title",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("예외 메세지")
                        )));
    }

    @Test
    @DisplayName("내용이 존재하지 않을 경우 HTTP status 400과 예외 메세지를 반환한다.")
    public void save_fail_invalid_content() throws Exception {
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest(1L, "게시물 제목", "", LocalDateTime.now());
        mockMvc.perform(post("/api/v1/boards")
                .content(objectMapper.writeValueAsString(boardCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("board/create/fail/invalid-content",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }
}
