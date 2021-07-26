package com.project.kodesalon.model.board.repository;

import com.project.kodesalon.model.board.domain.Board;

import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> selectBoards(final Long lastBoardId, final int size);
}
