package com.project.kodesalon.repository;

import com.project.kodesalon.domain.Board;

import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> selectBoards(final Long lastBoardId, final int size);
}
