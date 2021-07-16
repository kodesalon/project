package com.project.kodesalon.model.board.service;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.board.service.dto.BoardCreateRequest;
import com.project.kodesalon.model.board.service.dto.BoardSelectResponse;
import com.project.kodesalon.model.board.service.dto.BoardUpdateRequest;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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
        boardRepository.save(createdBoard);
    }

    @Transactional
    public void delete(final Long memberId, final Long boardId) {
        Board board = findById(boardId);
        board.delete(memberId);
    }

    @Transactional(readOnly = true)
    public BoardSelectResponse selectBoard(final Long boardId) {
        Board board = findById(boardId);
        return new BoardSelectResponse(board.getId(), board.getTitle(), board.getContent(), board.getCreatedDateTime(), board.getWriter().getId(), board.getWriter().getAlias());
    }

    @Transactional(readOnly = true)
    public List<BoardSelectResponse> selectBoards(Long lastBoardId, int size) {
        List<Board> boards = boardRepository.selectBoards(lastBoardId, size);

        return boards.stream()
                .map(board -> new BoardSelectResponse(board.getId(), board.getTitle(), board.getContent(), board.getCreatedDateTime(), board.getWriter().getId(), board.getWriter().getAlias()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateBoard(final Long memberId, final Long boardId, final BoardUpdateRequest boardUpdateRequest) {
        Title updateTitle = new Title(boardUpdateRequest.getUpdatedTitle());
        Content updateContent = new Content(boardUpdateRequest.getUpdatedContent());
        Board updatedBoard = findById(boardId);
        updatedBoard.updateTitleAndContent(memberId, updateTitle, updateContent);
    }

    private Board findById(final Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.info("존재하지 않는 게시물 식별자 boardId : {}", boardId);
                    throw new EntityNotFoundException(NOT_EXIST_BOARD);
                });
    }
}
