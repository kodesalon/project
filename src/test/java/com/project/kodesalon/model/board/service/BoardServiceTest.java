package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.dto.BoardCreateRequestDto;
import com.project.kodesalon.model.board.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.project.kodesalon.model.board.domain.BoardTest.CONTENT;
import static com.project.kodesalon.model.board.domain.BoardTest.CREATED_DATE_TIME;
import static com.project.kodesalon.model.board.domain.BoardTest.TITLE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Test
    @DisplayName("컨트롤러에서 게시판 생성 요청 Dto를 전달받아 게시판을 생성한다.")
    public void save() {
        BoardCreateRequestDto boardCreateRequestDto = new BoardCreateRequestDto(1L, TITLE, CONTENT, CREATED_DATE_TIME.toString());
        boardService.save(boardCreateRequestDto);
        verify(boardRepository, times(1)).save(any(Board.class));
    }
}
