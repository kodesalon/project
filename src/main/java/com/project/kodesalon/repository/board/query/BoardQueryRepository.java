package com.project.kodesalon.repository.board.query;

import com.project.kodesalon.repository.board.query.dto.BoardFlatQueryDto;

import java.util.List;

public interface BoardQueryRepository {
    List<BoardFlatQueryDto> selectQueryBoards(final Long lastBoardId, final int size);
}
