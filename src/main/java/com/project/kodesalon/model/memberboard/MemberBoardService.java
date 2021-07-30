package com.project.kodesalon.model.memberboard;

import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_MEMBER;

@Slf4j
@Service
public class MemberBoardService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public MemberBoardService(MemberRepository memberRepository, BoardRepository boardRepository) {
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
    }

    public Member findById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.info("회원 조회 단계에서 존재하지 않는 회원 식별자 memberId : {}", memberId);
                    throw new EntityNotFoundException(NOT_EXIST_MEMBER);
                });
    }

    public void deleteBoardByMemberId(final Long memberId) {
        boardRepository.deleteBoardByMemberId(memberId);
    }
}
