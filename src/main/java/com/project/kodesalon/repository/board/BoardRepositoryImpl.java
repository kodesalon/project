package com.project.kodesalon.repository.board;

import com.project.kodesalon.domain.board.Board;

import javax.persistence.EntityManager;
import java.util.List;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private static final int CHECK_NEXT_BOARD = 1;
    private final EntityManager entityManager;

    public BoardRepositoryImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Board> selectBoards(final Long lastBoardId, final int size) {
        String query = "select distinct b from Board b left outer join fetch b.images where b.id < :lastBoardId and b.deleted = false order by b.id desc";

        return entityManager.createQuery(query, Board.class)
                .setParameter("lastBoardId", lastBoardId)
                .setMaxResults(size + CHECK_NEXT_BOARD)
                .getResultList();
    }
}
