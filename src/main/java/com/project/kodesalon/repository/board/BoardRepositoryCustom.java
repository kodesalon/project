package com.project.kodesalon.repository.board;

import com.project.kodesalon.domain.board.Board;

import java.util.Deque;

public interface BoardRepositoryCustom {

    Deque<Board> selectBoards(final Long lastBoardId, final int size);
}
