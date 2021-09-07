package com.project.kodesalon.repository.board;

import com.project.kodesalon.domain.board.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.Optional;

import static com.project.kodesalon.domain.board.QBoard.board;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private static final int CHECK_NEXT_BOARD = 1;

    private final JPAQueryFactory jpaQueryFactory;

    public BoardRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Board> selectMyBoards(final Long memberId, final Long lastBoardId, final int size) {
        return jpaQueryFactory.selectDistinct(board)
                .from(board)
                .leftJoin(board.images).fetchJoin()
                .where(
                        board.id.lt(lastBoardId),
                        board.writer.id.eq(memberId)
                )
                .orderBy(board.id.desc())
                .limit((long) size + CHECK_NEXT_BOARD)
                .fetch();
    }

    @Override
    public void deleteBoardByMemberId(final Long memberId) {
        jpaQueryFactory.update(board)
                .set(board.deleted, true)
                .where(board.writer.id.eq(memberId))
                .execute();
    }

    @Override
    public Optional<Board> selectBoardById(final Long boardId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(board)
                .from(board)
                .where(board.id.eq(boardId))
                .fetchOne());
    }
}
