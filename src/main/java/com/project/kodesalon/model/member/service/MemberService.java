package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.repository.MemberRepository;
import com.project.kodesalon.model.member.service.dto.ChangePasswordRequest;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.service.dto.SelectMemberResponse;
import com.project.kodesalon.model.memberboard.MemberBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static com.project.kodesalon.common.ErrorCode.ALREADY_EXIST_MEMBER_ALIAS;
import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_MEMBER_ALIAS;

@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberBoardService memberBoardService;

    public MemberService(final MemberRepository memberRepository, MemberBoardService memberBoardService) {
        this.memberRepository = memberRepository;
        this.memberBoardService = memberBoardService;
    }

    @Transactional
    public void join(final CreateMemberRequest createMemberRequest) {
        String alias = createMemberRequest.getAlias();
        validateDuplicationOf(alias);
        Member saveMember = memberRepository.save(createMemberRequest.toMember());
        log.info("ID : {}, Alias : {} Member가 회원 가입 성공", saveMember.getId(), alias);
    }

    private void validateDuplicationOf(final String alias) {
        memberRepository.findMemberByAlias(new Alias(alias))
                .ifPresent(member -> {
                    log.info("{}는 이미 존재하는 Alias입니다.", alias);
                    throw new IllegalStateException(ALREADY_EXIST_MEMBER_ALIAS);
                });
    }

    @Transactional(readOnly = true)
    public SelectMemberResponse selectMember(final Long memberId) {
        Member member = memberBoardService.findById(memberId);
        return new SelectMemberResponse(member.getAlias(), member.getName(), member.getEmail(), member.getPhone());
    }

    @Transactional
    public void changePassword(final Long memberId, final ChangePasswordRequest changePasswordRequest) {
        Member member = memberBoardService.findById(memberId);
        member.changePassword(changePasswordRequest.getPassword());
    }

    @Transactional
    public void deleteMember(final Long memberId) {
        Member member = memberBoardService.findById(memberId);
        member.delete();
    }

    public Member findMemberByAlias(final String alias) {
        return memberRepository.findMemberByAlias(new Alias(alias))
                .orElseThrow(() -> {
                    log.info("{}인 Alias를 가진 사용자가 존재하지 않음", alias);
                    throw new EntityNotFoundException(NOT_EXIST_MEMBER_ALIAS);
                });
    }
}
