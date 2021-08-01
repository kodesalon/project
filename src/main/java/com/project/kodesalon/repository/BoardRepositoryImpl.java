package com.project.kodesalon.repository;

import com.project.kodesalon.domain.board.Board;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final EntityManager entityManager;

    public BoardRepositoryImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Board> selectBoards(Long lastBoardId, final int size) {
        if (Objects.isNull(lastBoardId)) {
            lastBoardId = Long.MAX_VALUE;
        }

        String query = "select b from Board b join fetch b.writer where b.id < :lastBoardId and b.deleted = false order by b.id desc";

        return entityManager.createQuery(query, Board.class)
                .setParameter("lastBoardId", lastBoardId)
                .setMaxResults(size)
                .getResultList();
    }
}
