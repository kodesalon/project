package com.project.kodesalon.repository.board;

import com.project.kodesalon.domain.board.Board;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardRepositoryCustom {

    @Transactional(readOnly = true)
    List<Board> selectBoards(final Long memberId, final Long lastBoardId, final long size);

    @Transactional
    void deleteBoardByMemberId(final Long memberId);
}
