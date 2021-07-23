package com.project.kodesalon.model.board.repository;

import com.project.kodesalon.model.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying(flushAutomatically = true)
    @Query("update Board b set b.deleted = true where b.writer.id = :memberId and b.deleted = false")
    void deleteBoardByMemberId(@Param("memberId") Long memberId);

    @Query("select b from Board b join fetch b.writer where b.id = :boardId")
    Optional<Board> selectBoardById(@Param("boardId") Long boardId);

    @Query(value = "SELECT * FROM board b INNER JOIN member m ON b.member_id = m.member_id " +
            "WHERE b.board_id < :lastBoardId AND b.deleted = FALSE ORDER BY b.board_id DESC LIMIT :size", nativeQuery = true)
    List<Board> selectBoards(@Param("lastBoardId") Long lastBoardId, @Param("size") int size);
}
