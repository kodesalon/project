package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardUpdateRequest;
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
    public void save(final Long memberId, final BoardCreateRequest boardCreateRequest) {
        Member member = memberService.findById(memberId);
        Board createdBoard = boardCreateRequest.toBoard(member);
        log.info("Member alias : {}, Board Id : {}", member.getAlias(), createdBoard.getId());
        boardRepository.save(createdBoard);
    }

    @Transactional
    public void delete(Long memberId, Long boardId) {
        Board board = findById(boardId);
        board.delete(memberId);
    }

    private Board findById(final Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.info("존재하지 않는 게시물 식별자 boardId : {}", boardId);
                    throw new EntityNotFoundException(NOT_EXIST_BOARD);
                });
    }

    @Transactional
    public void updateBoard(Long memberId, final Long boardId, final BoardUpdateRequest boardUpdateRequest) {
        Title updateTitle = new Title(boardUpdateRequest.getUpdatedTitle());
        Content updateContent = new Content(boardUpdateRequest.getUpdatedContent());
        Board updatedBoard = findBoardById(boardId);
        updatedBoard.updateTitleAndContent(memberId, updateTitle, updateContent);
    }

    private Board findBoardById(final Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.info("게시물 수정 단계에서 존재하지 않는 게시물 식별자 boardId : {}", boardId);
                    throw new EntityNotFoundException(NOT_EXIST_BOARD);
                });
    }
}
