package com.project.kodesalon.repository.board;

import com.project.kodesalon.domain.board.Board;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    @Transactional(readOnly = true)
    Optional<Board> selectBoard(final Long boardId);

    @Transactional(readOnly = true)
    List<Board> selectBoards(final Long lastBoardId, final long size);

    @Transactional(readOnly = true)
    List<Board> selectMyBoards(final Long memberId, final Long lastBoardId, final long size);

    @Transactional
    void deleteBoardByMemberId(final Long memberId);
}
