package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardDeleteRequest;
import com.project.kodesalon.model.board.service.dto.BoardSelectSingleResponse;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

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
    @DisplayName("컨트롤러에서 게시판 삭제 요청 Dto를 전달받아 게시물을 삭제한다.")
    void delete() {
        given(memberService.findById(anyLong())).willReturn(member);
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        BoardDeleteRequest boardDeleteRequest = new BoardDeleteRequest(1L, 1L);

        boardService.delete(boardDeleteRequest);

        verify(board, times(1)).delete(member);
    }

    @Test
    @DisplayName("컨트롤러에서 게시물 식별 번호를 전달받아 게시물을 조회하고 단일 게시물 조회 응답 DTO를 반환한다.")
    void selectBoard() {
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        BoardSelectSingleResponse boardSelectSingleResponse = boardService.selectBoard(1L);

        then(boardSelectSingleResponse).isNotNull();
        verify(boardRepository).findById(anyLong());
    }
}
