package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardUpdateRequest;
import com.project.kodesalon.model.board.service.dto.BoardUpdateResponse;
import com.project.kodesalon.model.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.project.kodesalon.model.member.domain.MemberTest.TEST_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    private final Board TEST_BOARD = new Board(new Title("게시물 제목"), new Content("게시물 내용"), TEST_MEMBER, LocalDateTime.now());
    private final BoardUpdateRequest BOARD_UPDATE_REQUEST = new BoardUpdateRequest(1L, "update title", "update content");
    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("컨트롤러에서 게시판 생성 요청 Dto를 전달받아 게시판을 생성한다.")
    public void save() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(TEST_MEMBER));
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest(1L, "게시물 제목", "게시물 작성", LocalDateTime.now());
        boardService.save(boardCreateRequest);
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    @DisplayName("컨트롤러에서 게시판 수정 요청 Dto를 전달받아 게시판을 수정한다.")
    void update() {
        given(memberRepository.existsById(anyLong())).willReturn(true);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(TEST_BOARD));

        BoardUpdateResponse boardUpdateResponse = boardService.updateBoard(1L, BOARD_UPDATE_REQUEST);

        then(boardUpdateResponse.getMessage()).isEqualTo("게시물 정보가 변경되었습니다");
    }

    @Test
    @DisplayName("게시물 수정 요청시 회원이 존재하지 않으면 예외를 발생시킵니다")
    void update_throws_exception_with_no_member() {
        given(memberRepository.existsById(anyLong())).willReturn(false);

        thenThrownBy(() -> boardService.updateBoard(1L, BOARD_UPDATE_REQUEST))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("찾으려는 회원이 없습니다");
    }

    @Test
    @DisplayName("게시물 수정 요청시 게시물이 존재하지 않으면 예외를 발생시킵니다")
    void update_throws_exception_with_no_board() {
        given(memberRepository.existsById(anyLong())).willReturn(true);
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> boardService.updateBoard(1L, BOARD_UPDATE_REQUEST))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("수정하려는 게시물이 없습니다");
    }
}
