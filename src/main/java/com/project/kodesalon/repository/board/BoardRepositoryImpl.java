package com.project.kodesalon.repository.board;

import com.project.kodesalon.domain.board.Board;

import javax.persistence.EntityManager;
import java.util.ArrayDeque;
import java.util.Deque;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private static final int CHECK_NEXT_BOARD = 1;
    private final EntityManager entityManager;

    public BoardRepositoryImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Deque<Board> selectBoards(Long lastBoardId, int size) {
        String query = "select b from Board b join fetch b.writer where b.id < :lastBoardId and b.deleted = false order by b.id desc";

        return new ArrayDeque<>(entityManager.createQuery(query, Board.class)
                .setParameter("lastBoardId", lastBoardId)
                .setMaxResults(size + CHECK_NEXT_BOARD)
                .getResultList());
    }
}