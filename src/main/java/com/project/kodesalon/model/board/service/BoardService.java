package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardDeleteRequest;
import com.project.kodesalon.model.board.service.dto.BoardSelectSingleResponse;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_BOARD;
import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_MEMBER;

@Slf4j
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public BoardService(final BoardRepository boardRepository, final MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void save(final BoardCreateRequest boardCreateRequest) {
        Member member = findMemberById(boardCreateRequest.getMemberId());
        Board createdBoard = boardCreateRequest.toBoard(member);
        member.addBoard(createdBoard);
        boardRepository.save(createdBoard);
    }

    public void delete(final BoardDeleteRequest boardDeleteRequest) {
        Board board = findBoardById(boardDeleteRequest.getBoardId());
        Member member = findMemberById(boardDeleteRequest.getMemberId());
        board.delete(member);
    }

    private Member findMemberById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.info("게시물 저장 단계에서 존재하지 않는 회원 식별자 memberId : {}", memberId);
                    throw new EntityNotFoundException(NOT_EXIST_MEMBER);
                });
    }

    public BoardSelectSingleResponse selectBoard(final Long boardId) {
        Board board = findBoardById(boardId);
        return new BoardSelectSingleResponse(board.getTitle(), board.getContent(), board.getCreatedDateTime(), board.getWriter());
    }

    private Board findBoardById(final Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.info("존재하지 않는 게시물 식별자 boardId : {}", boardId);
                    throw new EntityNotFoundException(NOT_EXIST_BOARD);
                });
    }
}
