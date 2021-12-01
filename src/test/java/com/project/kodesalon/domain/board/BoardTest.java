package com.project.kodesalon.domain.board;

import com.project.kodesalon.domain.board.vo.Content;
import com.project.kodesalon.domain.board.vo.Title;
import com.project.kodesalon.domain.image.Image;
import com.project.kodesalon.domain.member.Member;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_IMAGES_SIZE;
import static com.project.kodesalon.exception.ErrorCode.NOT_AUTHORIZED_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BoardTest {

    private final BDDSoftAssertions softly = new BDDSoftAssertions();
    private final LocalDateTime createdDateTime = LocalDateTime.now();
    private Board board;

    @Mock
    private Member member;

    @Mock
    private Member stranger;

    @BeforeEach
    void setUp() {
        board = new Board("게시물 제목", "게시물 내용", member, createdDateTime);
    }

    @Test
    @DisplayName("게시물의 제목, 내용, 작성자, 생성 시간, 수정 시간, 삭제 여부를 반환한다.")
    void getter() {
        softly.then(board.getTitle()).isEqualTo("게시물 제목");
        softly.then(board.getContent()).isEqualTo("게시물 내용");
        softly.then(board.getWriter()).isEqualTo(member);
        softly.then(board.getCreatedDateTime()).isEqualTo(createdDateTime);
        softly.then(board.getLastModifiedDateTime()).isEqualTo(createdDateTime);
        softly.then(board.isDeleted()).isFalse();
        softly.assertAll();
    }

    @Test
    @DisplayName("게시물 삭제 여부를 참으로 변경한다.")
    void delete() {
        given(member.getId()).willReturn(1L);

        board.delete(member.getId(), LocalDateTime.now());

        then(board.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("다른 회원이 게시물 삭제를 시도할 경우, 예외가 발생한다.")
    void delete_throw_exception_with_not_authorized_member() {
        given(member.getId()).willReturn(1L);
        given(stranger.getId()).willReturn(2L);

        thenIllegalArgumentException()
                .isThrownBy(() -> board.delete(stranger.getId(), LocalDateTime.now()))
                .withMessage(NOT_AUTHORIZED_MEMBER);
    }

    @Test
    @DisplayName("게시물의 제목과 내용을 전달받아 board의 제목과 내용을 변경한다.")
    void update_board() {
        Title updatedTitle = new Title("update title");
        Content updatedContent = new Content("update content");
        LocalDateTime lastModifiedDateTime = LocalDateTime.now();
        given(member.getId()).willReturn(1L);

        board.updateTitleAndContent(member.getId(), updatedTitle, updatedContent, lastModifiedDateTime);

        softly.then(board.getTitle()).isEqualTo(updatedTitle.value());
        softly.then(board.getContent()).isEqualTo(updatedContent.value());
        softly.then(board.getLastModifiedDateTime()).isEqualTo(lastModifiedDateTime);
        softly.assertAll();
    }

    @Test
    @DisplayName("다른 회원이 게시물 삭제를 시도할 경우, 예외가 발생한다.")
    void update_throw_exception_with_not_authorized_member() {
        Title updatedTitle = new Title("update title");
        Content updatedContent = new Content("update content");
        given(member.getId()).willReturn(1L);
        given(stranger.getId()).willReturn(2L);

        thenIllegalArgumentException()
                .isThrownBy(() -> board.updateTitleAndContent(stranger.getId(), updatedTitle, updatedContent, LocalDateTime.now()))
                .withMessage(NOT_AUTHORIZED_MEMBER);
    }

    @Test
    @DisplayName("이미지를 추가한다.")
    void addImage() {
        //given
        Image image = mock(Image.class);

        //when
        board.addImage(image);

        //then
        then(board.getImages()).hasSize(1);
    }

    @Test
    @DisplayName("5장 초과하여 이미지를 추가할 경우, 예외가 발생한다.")
    void addImage_throw_exception_image_size_bound() {
        Image image = mock(Image.class);
        for (int i = 0; i < 5; i++) {
            board.addImage(image);
        }

        thenIllegalArgumentException()
                .isThrownBy(() -> board.addImage(image))
                .withMessage(INVALID_BOARD_IMAGES_SIZE);
    }

    @Test
    @DisplayName("이미지를 제거한다.")
    void deleteImages() {
        Image image1 = mock(Image.class);
        Image image2 = mock(Image.class);
        board.addImage(image1);
        board.addImage(image2);

        board.deleteImages(Arrays.asList(image1, image2));

        then(board.getImages()).isEmpty();
    }
}
