package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardUpdateRequest;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_BOARD;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    private final BoardUpdateRequest BOARD_UPDATE_REQUEST = new BoardUpdateRequest("update title", "update content");

    @InjectMocks
    private BoardService boardService;

    @Mock
    private MemberService memberService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private Member member;

    @Mock
    private Board board;

    @Test
    @DisplayName("컨트롤러에서 게시판 생성 요청 Dto를 전달받아 게시판을 생성한다.")
    void save() {
        given(memberService.findById(anyLong())).willReturn(member);
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest("게시물 제목", "게시물 작성", LocalDateTime.now());

        boardService.save(anyLong(), boardCreateRequest);

        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("컨트롤러에서 회원 식별 번호, 게시물 식별 번호를 인자로 전달받아 게시물을 삭제한다.")
    void delete() {
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        boardService.delete(1L, anyLong());

        verify(board, times(1)).delete(anyLong());
    }

    @Test
    @DisplayName("컨트롤러에서 게시판 수정 요청 Dto를 전달받아 게시판을 수정한다.")
    void update() {
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(member.getId()).willReturn(1L);

        boardService.updateBoard(member.getId(), 1L, BOARD_UPDATE_REQUEST);

        verify(boardRepository, times(1)).findById(anyLong());
        verify(board, times(1)).updateTitleAndContent(anyLong(), any(Title.class), any(Content.class));
    }

    @Test
    @DisplayName("게시물 수정 요청시 게시물이 존재하지 않으면 예외를 발생시킵니다")
    void update_throws_exception_with_no_board() {
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> boardService.updateBoard(member.getId(), 1L, BOARD_UPDATE_REQUEST))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NOT_EXIST_BOARD);
    }
}
