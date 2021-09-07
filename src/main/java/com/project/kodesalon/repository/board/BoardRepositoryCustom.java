package com.project.kodesalon.repository.board;

import com.project.kodesalon.domain.board.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    List<Board> selectMyBoards(final Long memberId, final Long lastBoardId, final int size);

    void deleteBoardByMemberId(final Long memberId);

    Optional<Board> selectBoardById(final Long boardId);
}
