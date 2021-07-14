package com.project.kodesalon.model.board.repository;

import com.project.kodesalon.model.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Board b set b.deleted = true where b.writer.id = :memberId")
    void deleteBoardByMemberId(@Param("memberId") Long memberId);

    @Query("select b from Board b join fetch b.writer where b.id = :boardId")
    Optional<Board> selectBoardById(@Param("boardId") Long boardId);

    @Query("select b from Board b join fetch b.writer where b.id > :lastBoardId and b.deleted = false order by b.id desc")
    List<Board> findTop10Boards(@Param("lastBoardId") Long lastBoardId);
}
