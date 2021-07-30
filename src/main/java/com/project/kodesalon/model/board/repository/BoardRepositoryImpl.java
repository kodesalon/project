package com.project.kodesalon.model.board.repository;

import com.project.kodesalon.model.board.domain.Board;

import javax.persistence.EntityManager;
import java.util.List;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final EntityManager entityManager;

    public BoardRepositoryImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Board> selectBoards(final Long lastBoardId, final int size) {
        if (lastBoardId == null) {
            return selectBoardsAtFirst(size);
        }

        String query = "select b from Board b join fetch b.writer where b.id < :lastBoardId and b.deleted = false order by b.id desc";

        return entityManager.createQuery(query, Board.class)
                .setParameter("lastBoardId", lastBoardId)
                .setMaxResults(size)
                .getResultList();
    }

    private List<Board> selectBoardsAtFirst(final int size) {
        String query = "select b from Board b join fetch b.writer where b.deleted = false order by b.id desc";

        return entityManager.createQuery(query, Board.class)
                .setMaxResults(size)
                .getResultList();
    }
}
