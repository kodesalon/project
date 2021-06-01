package com.project.kodesalon.model.board.controller;

import com.project.kodesalon.common.GlobalExceptionHandler;
import com.project.kodesalon.model.board.domain.dto.BoardCreateRequestDto;
import com.project.kodesalon.model.board.exception.ForbiddenException;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentRequest;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
        String requestBody = "{ \"memberId\": 1, \"title\": \"게시물 제목\", \"content\": \"게시물 내용\", \"createdDateTime\": \"2021-06-01T23:59:59.999999\"}";
        mockMvc.perform(post("/api/v1/boards/")
                .content(requestBody)
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
    @DisplayName("제목이 존재하지 않을 경우 HTTP status 403과 예외 메세지를 반환한다.")
    public void save_fail_invalid_title() throws Exception {
        String requestBody = "{ \"memberId\": 1, \"title\": \"\", \"content\": \"게시물 내용\", \"createdDateTime\": \"2021-06-01T23:59:59.999999\"}";
        doThrow(new ForbiddenException("제목에 공백 아닌 1자 이상의 문자를 입력해주세요.")).when(boardService).save(any(BoardCreateRequestDto.class));
        mockMvc.perform(post("/api/v1/boards/")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(document("board/create/fail/invalid-title",
                        getDocumentRequest(),
                        getDocumentResponse()));
    }
}
