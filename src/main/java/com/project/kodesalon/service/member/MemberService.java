package com.project.kodesalon.service.member;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.image.Image;
import com.project.kodesalon.domain.member.Member;
import com.project.kodesalon.domain.member.vo.Alias;
import com.project.kodesalon.repository.board.BoardRepository;
import com.project.kodesalon.repository.member.MemberRepository;
import com.project.kodesalon.service.dto.request.LoginRequest;
import com.project.kodesalon.service.dto.request.MemberChangePasswordRequest;
import com.project.kodesalon.service.dto.request.MemberCreateRequest;
import com.project.kodesalon.service.dto.request.MemberDeleteRequest;
import com.project.kodesalon.service.dto.response.BoardImageResponse;
import com.project.kodesalon.service.dto.response.BoardSelectResponse;
import com.project.kodesalon.service.dto.response.MemberSelectResponse;
import com.project.kodesalon.service.dto.response.MultiBoardSelectResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.kodesalon.config.interceptor.LoginInterceptor.LOGIN_MEMBER;
import static com.project.kodesalon.exception.ErrorCode.ALREADY_EXIST_MEMBER_ALIAS;
import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_MEMBER;
import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_MEMBER_ALIAS;

@Slf4j
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public MemberService(final MemberRepository memberRepository, BoardRepository boardRepository) {
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional
    public void join(final MemberCreateRequest memberCreateRequest) {
        String alias = memberCreateRequest.getAlias();
        validateDuplicationOf(alias);
        Member saveMember = saveMember(memberCreateRequest);
        log.info("ID : {}, Alias : {} Member??? ?????? ?????? ??????", saveMember.getId(), alias);
    }

    private void validateDuplicationOf(final String alias) {
        memberRepository.findMemberByAlias(new Alias(alias))
                .ifPresent(member -> {
                    log.info("{}??? ?????? ???????????? Alias?????????.", alias);
                    throw new IllegalStateException(ALREADY_EXIST_MEMBER_ALIAS);
                });
    }

    private Member saveMember(final MemberCreateRequest memberCreateRequest) {
        try {
            return memberRepository.save(memberCreateRequest.toMember());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(ALREADY_EXIST_MEMBER_ALIAS);
        }
    }

    @Transactional(readOnly = true)
    public void login(final LoginRequest loginRequest, final HttpSession session) {
        String alias = loginRequest.getAlias();
        Member member = findMemberByAlias(alias);

        String password = loginRequest.getPassword();
        member.login(password);

        Long memberId = member.getId();
        String memberAlias = member.getAlias();
        session.setAttribute(LOGIN_MEMBER, memberId);
        log.info("?????? ?????? ?????? : {}, ?????? : {} ????????? ??????", memberId, memberAlias);
    }

    private Member findMemberByAlias(final String alias) {
        return memberRepository.findMemberByAlias(new Alias(alias))
                .orElseThrow(() -> {
                    log.info("{}??? Alias??? ?????? ???????????? ???????????? ??????", alias);
                    throw new EntityNotFoundException(NOT_EXIST_MEMBER_ALIAS);
                });
    }

    @Transactional(readOnly = true)
    public MemberSelectResponse selectMember(final Long memberId, final Long lastBoardId, final int size) {
        Member member = findById(memberId);
        List<Board> boards = boardRepository.selectMyBoards(memberId, lastBoardId, size);
        List<BoardSelectResponse> boardSelectResponses = mapToBoardSelectResponses(member, boards);
        MultiBoardSelectResponse<BoardSelectResponse> multiBoardSelectResponse = new MultiBoardSelectResponse<>(boardSelectResponses, size);
        return new MemberSelectResponse(member.getAlias(), member.getName(), member.getEmail(), member.getPhone(), multiBoardSelectResponse);
    }

    private List<BoardSelectResponse> mapToBoardSelectResponses(final Member member, final List<Board> boards) {
        return boards.stream()
                .map(board -> new BoardSelectResponse(board.getId(), board.getTitle(), board.getContent(),
                        board.getCreatedDateTime(), member.getId(), member.getAlias(), mapToBoardImageResponses(board.getImages())))
                .collect(Collectors.toList());
    }

    private List<BoardImageResponse> mapToBoardImageResponses(final List<Image> images) {
        return images.stream()
                .map(image -> new BoardImageResponse(image.getId(), image.getUrl()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void changePassword(final Long memberId, final MemberChangePasswordRequest memberChangePasswordRequest) {
        Member member = findById(memberId);
        member.changePassword(memberChangePasswordRequest.getPassword(), memberChangePasswordRequest.getLastModifiedDateTime());
    }

    @Transactional
    public void deleteMember(final Long memberId, final MemberDeleteRequest memberDeleteRequest) {
        Member member = findById(memberId);
        boardRepository.deleteBoardByMemberId(memberId);
        member.delete(memberDeleteRequest.getDeletedDateTime());
    }

    @Transactional(readOnly = true)
    public Member findById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.info("?????? ?????? ???????????? ???????????? ?????? ?????? ????????? memberId : {}", memberId);
                    throw new EntityNotFoundException(NOT_EXIST_MEMBER);
                });
    }
}
