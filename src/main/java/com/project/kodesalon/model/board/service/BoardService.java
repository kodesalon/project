package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardDeleteRequest;
import com.project.kodesalon.model.board.service.dto.BoardSelectSingleResponse;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_BOARD;

@Slf4j
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;

    public BoardService(final BoardRepository boardRepository, final MemberService memberService) {
        this.boardRepository = boardRepository;
        this.memberService = memberService;
    }

    @Transactional
    public void save(final BoardCreateRequest boardCreateRequest) {
        Member member = memberService.findById(boardCreateRequest.getMemberId());
        Board createdBoard = boardCreateRequest.toBoard(member);
        boardRepository.save(createdBoard);
    }

    public void delete(final BoardDeleteRequest boardDeleteRequest) {
        Board board = findById(boardDeleteRequest.getBoardId());
        Member member = memberService.findById(boardDeleteRequest.getMemberId());
        board.delete(member);
    }

    public BoardSelectSingleResponse selectBoard(final Long boardId) {
        Board board = findById(boardId);
        return new BoardSelectSingleResponse(board.getTitle(), board.getContent(), board.getCreatedDateTime(), board.getWriter());
    }

    private Board findById(final Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.info("존재하지 않는 게시물 식별자 boardId : {}", boardId);
                    throw new EntityNotFoundException(NOT_EXIST_BOARD);
                });
    }
}
