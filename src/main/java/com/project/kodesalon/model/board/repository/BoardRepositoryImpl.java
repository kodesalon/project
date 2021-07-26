package com.project.kodesalon.model.board.repository;

import com.project.kodesalon.model.board.domain.Board;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Board> selectBoards(final Long lastBoardId, final int size) {
        String query = "select b from Board b join fetch b.writer where b.id < :lastBoardId and b.deleted = false order by b.id desc";

        return em.createQuery(query, Board.class)
                .setParameter("lastBoardId", lastBoardId)
                .setMaxResults(size)
                .getResultList();
    }
}
