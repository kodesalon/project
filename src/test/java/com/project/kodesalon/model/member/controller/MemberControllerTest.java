package com.project.kodesalon.model.member.controller;

import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class,
        SpringExtension.class, MockitoExtension.class})
@SpringBootTest
public class MemberControllerTest {
    private static final String LOGIN_REQUEST_JSON = "{\"alias\" : \"alias\", \"password\" : \"Password123!!\"}";

    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("로그인이 성공하면 Alias, ID, Http Status 200를 Response 합니다.")
    void login_controller_return_success_response() throws Exception {
        when(memberService.login(any(LoginRequestDto.class)))
                .thenReturn(new ResponseEntity<>(new LoginResponseDto(1L, "alias"), HttpStatus.OK));

        this.mockMvc.perform(post("/api/v1/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(LOGIN_REQUEST_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("login",
                        requestFields(
                                fieldWithPath("alias").description("로그인 할 alias"),
                                fieldWithPath("password").description("로그인 할 패스워드")
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("Member 식별자"),
                                fieldWithPath("alias").description("member alias"),
                                fieldWithPath("message").description("error message"))));
    }
}
