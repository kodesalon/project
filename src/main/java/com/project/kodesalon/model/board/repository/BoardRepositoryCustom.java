package com.project.kodesalon.model.board.repository;

import com.project.kodesalon.model.board.domain.Board;

import java.util.Deque;

public interface BoardRepositoryCustom {

    Deque<Board> selectBoards(final Long lastBoardId, final int size);
}
