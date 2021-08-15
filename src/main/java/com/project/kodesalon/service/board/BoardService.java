package com.project.kodesalon.service.board;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.board.vo.Content;
import com.project.kodesalon.domain.board.vo.Title;
import com.project.kodesalon.domain.image.Image;
import com.project.kodesalon.domain.member.Member;
import com.project.kodesalon.repository.board.BoardRepository;
import com.project.kodesalon.service.S3Uploader;
import com.project.kodesalon.service.dto.request.BoardCreateRequest;
import com.project.kodesalon.service.dto.request.BoardDeleteRequest;
import com.project.kodesalon.service.dto.request.BoardUpdateRequest;
import com.project.kodesalon.service.dto.response.BoardImageResponse;
import com.project.kodesalon.service.dto.response.BoardSelectResponse;
import com.project.kodesalon.service.dto.response.MultiBoardSelectResponse;
import com.project.kodesalon.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_BOARD;

@Slf4j
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final S3Uploader s3Uploader;
    private final String directory;

    public BoardService(final BoardRepository boardRepository, final MemberService memberService, final S3Uploader s3Uploader,
                        @Value("${cloud.aws.s3.image.directory}") final String directory) {
        this.boardRepository = boardRepository;
        this.memberService = memberService;
        this.s3Uploader = s3Uploader;
        this.directory = directory;
    }

    @Transactional
    public void save(final Long memberId, final BoardCreateRequest boardCreateRequest) {
        Member member = memberService.findById(memberId);
        Board createdBoard = boardCreateRequest.toBoard(member);
        log.info("Member alias : {}, Board Id : {}", member.getAlias(), createdBoard.getId());

        for (MultipartFile multipartFile : boardCreateRequest.getImages()) {
            String url = s3Uploader.upload(multipartFile, directory);
            Image image = new Image(url, createdBoard);
            log.info("image id : {}", image.getId());
        }

        boardRepository.save(createdBoard);
    }

    @Transactional
    public void delete(final Long memberId, final Long boardId, final BoardDeleteRequest boardDeleteRequest) {
        Board board = findById(boardId);
        board.delete(memberId, boardDeleteRequest.getDeletedDateTime());
    }

    @Transactional(readOnly = true)
    public BoardSelectResponse selectBoard(final Long boardId) {
        Board board = boardRepository.selectBoardById(boardId)
                .orElseThrow(() -> {
                    log.info("존재하지 않는 게시물 식별자 boardId : {}", boardId);
                    throw new EntityNotFoundException(NOT_EXIST_BOARD);
                });

        List<BoardImageResponse> boardImages = board.getImages().stream()
                .map(image -> new BoardImageResponse(image.getId(), image.getUrl()))
                .collect(Collectors.toList());

        return new BoardSelectResponse(board.getId(), board.getTitle(), board.getContent(), board.getCreatedDateTime(), board.getWriter().getId(), board.getWriter().getAlias(), boardImages);
    }

    @Transactional(readOnly = true)
    public MultiBoardSelectResponse selectBoards(final Long lastBoardId, final int size) {
        List<Board> boards = boardRepository.selectBoards(lastBoardId, size);

        List<BoardSelectResponse> boardSelectResponses = boards.stream()
                .map(board -> new BoardSelectResponse(board.getId(), board.getTitle(), board.getContent(), board.getCreatedDateTime(),
                        board.getWriter().getId(), board.getWriter().getAlias(), getBoardImageResponses(board)))
                .collect(Collectors.toList());

        return new MultiBoardSelectResponse(boardSelectResponses, size);
    }

    private List<BoardImageResponse> getBoardImageResponses(final Board board) {
        return board.getImages()
                .stream()
                .map(image -> new BoardImageResponse(image.getId(), image.getUrl()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateBoard(Long memberId, final Long boardId, final BoardUpdateRequest boardUpdateRequest) {
        Title updateTitle = new Title(boardUpdateRequest.getTitle());
        Content updateContent = new Content(boardUpdateRequest.getContent());
        Board updatedBoard = findById(boardId);
        updatedBoard.updateTitleAndContent(memberId, updateTitle, updateContent, boardUpdateRequest.getLastModifiedDateTime());
    }

    public Board findById(final Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.info("존재하지 않는 게시물 식별자 boardId : {}", boardId);
                    throw new EntityNotFoundException(NOT_EXIST_BOARD);
                });
    }

    @Transactional(readOnly = true)
    public MultiBoardSelectResponse selectMyBoards(final Long memberId, final Long lastBoardId, final int size) {
        List<Board> myBoards = boardRepository.selectMyBoards(memberId, lastBoardId, size);

        List<BoardSelectResponse> boardSelectResponses = myBoards.stream()
                .map(board -> new BoardSelectResponse(board.getId(), board.getTitle(), board.getContent(), board.getCreatedDateTime(),
                        board.getWriter().getId(), board.getWriter().getAlias(), getBoardImageResponses(board)))
                .collect(Collectors.toList());

        return new MultiBoardSelectResponse(boardSelectResponses, size);
    }
}
