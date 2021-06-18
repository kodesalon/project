package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.repository.MemberRepository;
import com.project.kodesalon.model.member.service.dto.ChangePasswordRequest;
import com.project.kodesalon.model.member.service.dto.ChangePasswordResponse;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.service.dto.LoginRequest;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import com.project.kodesalon.model.member.service.dto.SelectMemberResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static com.project.kodesalon.common.ErrorCode.ALREADY_EXIST_MEMBER_ALIAS;
import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_MEMBER;

@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(final LoginRequest loginRequest) {
        String alias = loginRequest.getAlias();
        Member member = findMemberByAlias(alias);
        String password = loginRequest.getPassword();
        member.login(password);
        log.info("ID : {}, Alias : {} Member 로그인", member.getId(), member.getAlias());
        return new LoginResponse(member.getId(), member.getAlias());
    }

    private Member findMemberByAlias(final String alias) {
        return memberRepository.findMemberByAlias(new Alias(alias))
                .orElseThrow(() -> {
                    log.info("{}인 Alias를 가진 사용자가 존재하지 않음", alias);
                    throw new EntityNotFoundException(ALREADY_EXIST_MEMBER_ALIAS);
                });
    }

    @Transactional
    public LoginResponse join(final CreateMemberRequest createMemberRequest) {
        String alias = createMemberRequest.getAlias();
        validateDuplicationOf(alias);
        Member saveMember = memberRepository.save(createMemberRequest.toMember());
        log.info("ID : {}, Alias : {} Member가 회원 가입 성공", saveMember.getId(), saveMember.getAlias());
        return new LoginResponse(saveMember.getId(), saveMember.getAlias());
    }

    private void validateDuplicationOf(String alias) {
        memberRepository.findMemberByAlias(new Alias(alias))
                .ifPresent(member -> {
                    log.info("{}는 이미 존재하는 Alias입니다.", alias);
                    throw new IllegalStateException(ALREADY_EXIST_MEMBER_ALIAS);
                });
    }

    @Transactional(readOnly = true)
    public SelectMemberResponse selectMember(final Long memberId) {
        Member selectedMember = findById(memberId);
        return new SelectMemberResponse(selectedMember.getAlias(), selectedMember.getName(), selectedMember.getEmail(), selectedMember.getPhone());
    }

    @Transactional
    public ChangePasswordResponse changePassword(final Long memberId, final ChangePasswordRequest changePasswordRequest) {
        Member member = findById(memberId);
        member.changePassword(changePasswordRequest.getPassword());
        return new ChangePasswordResponse("비밀번호 변경 성공하였습니다.");
    }

    private Member findById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.info("회원 조회 단계에서 존재하지 않는 회원 식별자 memberId : {}", memberId);
                    throw new EntityNotFoundException(NOT_EXIST_MEMBER);
                });
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = findById(memberId);
        member.delete();
    }
}
