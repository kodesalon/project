package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.project.kodesalon.model.member.domain.MemberTest.TEST_MEMBER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("컨트롤러에서 게시판 생성 요청 Dto를 전달받아 게시판을 생성한다.")
    void save() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(TEST_MEMBER));
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest(1L, "게시물 제목", "게시물 작성", LocalDateTime.now());
        boardService.save(boardCreateRequest);
        verify(boardRepository).save(any(Board.class));
    }
}
