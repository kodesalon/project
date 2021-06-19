package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardSelectSingleResponse;
import com.project.kodesalon.model.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.project.kodesalon.model.member.domain.MemberTest.TEST_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    private final List<Board> boards = createBoards();

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
    @DisplayName("컨트롤러에서 게시물 식별 번호를 전달받아 게시물을 조회하고 달인 게시물 조회 응답 DTO를 반환한다.")
    void selectBoard() {
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(boards.get(0)));

        BoardSelectSingleResponse boardSelectSingleResponse = boardService.selectBoard(1L);

        then(boardSelectSingleResponse).isNotNull();
        verify(boardRepository).findById(anyLong());
    }

    private List<Board> createBoards() {
        List<Board> boards = new ArrayList<>();
        String title = "게시물 제목";
        String content = "게시물 내용";
        LocalDateTime createdDateTime = LocalDateTime.now();

        for (int i = 0; i < 11; i++) {
            boards.add(new Board(new Title(title), new Content(content), TEST_MEMBER, createdDateTime));
        }

        return boards;
    }
}
