package com.project.kodesalon.repository.board;

import com.project.kodesalon.domain.board.Board;

import java.util.List;

public interface BoardRepositoryCustom {

    List<Board> selectBoards(final Long lastBoardId, final int size);

    List<Board> selectMyBoards(final Long memberId, final Long lastBoardId, final int size);
}
