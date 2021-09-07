package com.project.kodesalon.repository.board;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.repository.board.query.BoardQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom, BoardQueryRepository {
}
