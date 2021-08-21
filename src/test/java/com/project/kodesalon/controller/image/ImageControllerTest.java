package com.project.kodesalon.controller.image;

import com.project.kodesalon.config.argumentresolver.LoginMemberArgumentResolver;
import com.project.kodesalon.config.interceptor.LoginInterceptor;
import com.project.kodesalon.exception.GlobalExceptionHandler;
import com.project.kodesalon.service.board.BoardService;
import com.project.kodesalon.service.image.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.project.kodesalon.exception.ErrorCode.INVALID_IMAGE;
import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_IMAGE;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentRequest;
import static com.project.kodesalon.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class ImageControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ImageController imageController;

    @Mock
    private ImageService imageService;

    @Mock
    private BoardService boardService;

    @Mock
    private LoginInterceptor loginInterceptor;

    @Mock
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setCustomArgumentResolvers(loginMemberArgumentResolver)
                .addInterceptors(loginInterceptor)
                .apply(documentationConfiguration(restDocumentation))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        given(loginInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any()))
                .willReturn(true);
        given(loginMemberArgumentResolver.supportsParameter(any(MethodParameter.class))).willReturn(true);
        given(loginMemberArgumentResolver.resolveArgument(any(MethodParameter.class), any(ModelAndViewContainer.class),
                any(NativeWebRequest.class), any(WebDataBinderFactory.class))).willReturn(1L);
    }

    @Test
    @DisplayName("저장할 여러 개의 이미지 파일을 인자로 요청받아, 이미지를 추가하고 HTTP 200 상태를 반환한다.")
    void save() throws Exception {
        Long boardId = 1L;
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "<<png data>>".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.png", "image/png", "<<png data>>".getBytes());

        mockMvc.perform(fileUpload("/api/v1/images/{boardId}", boardId).file(image1).file(image2))
                .andExpect(status().isOk())
                .andDo(document("image/create/success",
                        getDocumentRequest(),
                        pathParameters(
                                parameterWithName("boardId").description("이미지를 추가할 게시물의 식별 번호")
                        ),
                        requestParts(
                                partWithName("images").description("추가 하려는 이미지"))));
    }

    @Test
    @DisplayName("추가하려는 이미지가 유효하지 않을 경우, HTTP 400 상태와 예외 코드를 반환한다.")
    void save_throw_exception_with_invalid_image() throws Exception {
        Long boardId = 1L;
        MockMultipartFile image1 = new MockMultipartFile("images", "image1.png", "image/png", "<<png data>>".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("images", "image2.png", "image/png", "<<png data>>".getBytes());
        willThrow(new IllegalArgumentException(INVALID_IMAGE)).given(boardService).addImage(any(), anyList());

        mockMvc.perform(fileUpload("/api/v1/images/{boardId}", boardId).file(image1).file(image2))
                .andExpect(status().isBadRequest())
                .andDo(document("image/create/fail/invalid-file",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").description("유효하지 않은 이미지 파일에 대한 예외 코드"))));
    }

    @Test
    @DisplayName("삭제할 이미지의 리스트를 요청받아, 이미지를 삭제하고 HTTP 200 상태를 반환한다.")
    void remove() throws Exception {
        Object[] deleteIds = {1L, 2L, 3L};

        mockMvc.perform(delete("/api/v1/images/{imageIds}", deleteIds))
                .andExpect(status().isOk())
                .andDo(document("image/delete/success",
                        getDocumentRequest(),
                        pathParameters(
                                parameterWithName("imageIds").description("삭제할 이미지 리스트"))));
    }

    @Test
    @DisplayName("삭제할 이미지가 존재하지 않을 경우, 이미지를 삭제하고 HTTP 400 상태와 예외 코드를 반환한다.")
    void remove_throw_exception_with_not_exist_image() throws Exception {
        Object[] deleteIds = {1L, 2L, 3L};

        willThrow(new IllegalArgumentException(NOT_EXIST_IMAGE)).given(imageService).delete(anyList());

        mockMvc.perform(delete("/api/v1/images/{imageIds}", deleteIds))
                .andExpect(status().isBadRequest())
                .andDo(document("image/delete/fail/not-exist-image",
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").description("존재하지 않는 이미지에 대한 예외 코드"))));
    }
}
