package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardDeleteRequest;
import com.project.kodesalon.model.board.service.dto.BoardUpdateRequest;
import com.project.kodesalon.model.board.service.dto.BoardUpdateResponse;
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

import static com.project.kodesalon.common.ErrorCode.NOT_AUTHORIZED_MEMBER;
import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_BOARD;
import static com.project.kodesalon.model.member.domain.MemberTest.TEST_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    private final Board TEST_BOARD = new Board(new Title("게시물 제목"), new Content("게시물 내용"), TEST_MEMBER, LocalDateTime.now());
    private final BoardUpdateRequest BOARD_UPDATE_REQUEST = new BoardUpdateRequest(1L, "update title", "update content");

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
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest(1L, "게시물 제목", "게시물 작성", LocalDateTime.now());

        boardService.save(boardCreateRequest);

        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    @DisplayName("컨트롤러에서 게시판 수정 요청 Dto를 전달받아 게시판을 수정한다.")
    void update() {
        given(memberService.findById(anyLong())).willReturn(TEST_MEMBER);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(TEST_BOARD));

        BoardUpdateResponse boardUpdateResponse = boardService.updateBoard(1L, BOARD_UPDATE_REQUEST);

        then(boardUpdateResponse.getMessage()).isEqualTo("게시물 정보가 변경되었습니다");
    }

    @Test
    @DisplayName("게시물 수정 요청시 회원이 존재하지 않으면 예외를 발생시킵니다")
    void update_throws_exception_with_no_member() {
        given(memberService.findById(anyLong())).willThrow(new EntityNotFoundException(NOT_AUTHORIZED_MEMBER));

        thenThrownBy(() -> boardService.updateBoard(1L, BOARD_UPDATE_REQUEST))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NOT_AUTHORIZED_MEMBER);
    }

    @Test
    @DisplayName("게시물 수정 요청시 게시물이 존재하지 않으면 예외를 발생시킵니다")
    void update_throws_exception_with_no_board() {
        given(memberService.findById(anyLong())).willReturn(TEST_MEMBER);
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> boardService.updateBoard(1L, BOARD_UPDATE_REQUEST))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NOT_EXIST_BOARD);
    }

    @Test
    @DisplayName("컨트롤러에서 게시판 삭제 요청 Dto를 전달받아 게시물을 삭제한다.")
    void delete() {
        given(memberService.findById(anyLong())).willReturn(member);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(1L, 1L);

        boardService.delete(boardDeleteRequest);

        verify(board, times(1)).delete(member);
    }
}
