package com.project.kodesalon.repository.board.query;

import com.project.kodesalon.repository.board.query.dto.BoardFlatQueryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.project.kodesalon.domain.board.QBoard.board;
import static com.project.kodesalon.domain.image.QImage.image;

public class BoardQueryRepositoryImpl implements BoardQueryRepository {

    private static final int CHECK_NEXT_BOARD = 1;

    private final JPAQueryFactory jpaQueryFactory;

    public BoardQueryRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<BoardFlatQueryDto> selectQueryBoards(final Long lastBoardId, final int size) {
        return jpaQueryFactory.selectDistinct(Projections
                        .constructor(BoardFlatQueryDto.class,
                                board.id, board.title, board.content,
                                board.createdDateTime, board.writer.id,
                                board.writer.alias, image.id, image.url))
                .from(board)
                .innerJoin(board.writer)
                .leftJoin(image).on(board.id.eq(image.board.id))
                .where(board.id.lt(lastBoardId))
                .orderBy(board.id.desc())
                .limit((long) size + CHECK_NEXT_BOARD)
                .fetch();
    }
}
