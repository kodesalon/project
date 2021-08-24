package com.project.kodesalon.repository.board;

import com.project.kodesalon.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    @Modifying(flushAutomatically = true)
    @Query("update Board b set b.deleted = true where b.writer.id = :memberId and b.deleted = false")
    void deleteBoardByMemberId(@Param("memberId") final Long memberId);

    @Query("select b from Board b join fetch b.writer left outer join b.images where b.id = :boardId")
    Optional<Board> selectBoardById(@Param("boardId") final Long boardId);
}