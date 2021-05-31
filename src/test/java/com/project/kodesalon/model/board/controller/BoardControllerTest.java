package com.project.kodesalon.model.board.controller;

import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.model.board.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentRequest;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
public class BoardControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private BoardController boardController;

    @Mock
    private BoardService boardService;

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
        String requestBody = "{ \"memberId\": 1, \"title\": \"게시물 제목\", \"content\": \"게시물 내용\", \"createdDateTime\": \"작성 날짜\"}";
        mockMvc.perform(post("/api/v1/boards/")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("board/create/success",
                        getDocumentRequest(),
                        requestFields(
                                fieldWithPath("memberId").description("작성자 식별 번호"),
                                fieldWithPath("title").description("게시물 제목"),
                                fieldWithPath("content").description("게시물 내용"),
                                fieldWithPath("createdDateTime").description("게시물 작성 날짜")
                        ))
                );
    }
}
