package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardUpdateRequest;
import com.project.kodesalon.model.board.service.dto.BoardUpdateResponse;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_BOARD;
import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_MEMBER;

@Slf4j
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void save(BoardCreateRequest boardCreateRequest) {
        Long memberId = boardCreateRequest.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.info("게시물 저장 단계에서 존재하지 않는 회원 식별자 memberId : {}", memberId);
                    throw new NoSuchElementException("찾으려는 회원이 없습니다");
                });

        Board createdBoard = boardCreateRequest.toBoard(member);
        member.addBoard(createdBoard);
        boardRepository.save(createdBoard);
    }

    @Transactional
    public BoardUpdateResponse updateBoard(final Long boardId, final BoardUpdateRequest boardUpdateRequest) {
        Long memberId = boardUpdateRequest.getMemberId();

        if (!memberRepository.existsById(memberId)) {
            log.info("게시물 수정 단계에서 존재하지 않는 회원 식별자 memberId : {}", memberId);
            throw new EntityNotFoundException(NOT_EXIST_MEMBER);
        }

        Title updateTitle = new Title(boardUpdateRequest.getUpdatedTitle());
        Content updateContent = new Content(boardUpdateRequest.getUpdatedContent());
        Board updatedBoard = findBoardById(boardId);

        updatedBoard.updateTitleAndContent(updateTitle, updateContent);
        return new BoardUpdateResponse("게시물 정보가 변경되었습니다");
    }

    private Board findBoardById(final Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.info("게시물 수정 단계에서 존재하지 않는 게시물 식별자 boardId : {}", boardId);
                    throw new EntityNotFoundException(NOT_EXIST_BOARD);
                });
    }
}
