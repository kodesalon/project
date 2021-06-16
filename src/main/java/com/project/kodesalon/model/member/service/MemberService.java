package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.repository.MemberRepository;
import com.project.kodesalon.model.member.service.dto.ChangePasswordRequest;
import com.project.kodesalon.model.member.service.dto.ChangePasswordResponse;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.service.dto.DeleteMemberResponseDto;
import com.project.kodesalon.model.member.service.dto.LoginRequest;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import com.project.kodesalon.model.member.service.dto.SelectMemberResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) throws HttpClientErrorException {
        String alias = loginRequest.getAlias();
        Member member = memberRepository.findMemberByAlias(new Alias(alias))
                .orElseThrow(() -> {
                    log.info("{}인 Alias를 가진 사용자가 존재하지 않음", alias);
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하는 아이디를 입력해주세요.");
                });

        String password = loginRequest.getPassword();
        if (!member.hasSamePassword(new Password(password))) {
            log.info("{}의 Password가 일치하지 않음", alias);
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "비밀 번호가 일치하지 않습니다.");
        }

        log.info("ID : {}, Alias : {} Member 로그인", member.getId(), member.getAlias());
        return new LoginResponse(member.getId(), member.getAlias());
    }

    @Transactional
    public LoginResponse join(CreateMemberRequest createMemberRequest) {
        String alias = createMemberRequest.getAlias();
        memberRepository.findMemberByAlias(new Alias(alias))
                .ifPresent(member -> {
                    log.info("{}는 이미 존재하는 Alias입니다.", alias);
                    throw new IllegalStateException("이미 존재하는 아이디입니다");
                });

        Member saveMember = memberRepository.save(createMemberRequest.toMember());
        log.info("ID : {}, Alias : {} Member가 회원 가입 성공", saveMember.getId(), saveMember.getAlias());
        return new LoginResponse(saveMember.getId(), saveMember.getAlias());
    }

    @Transactional(readOnly = true)
    public SelectMemberResponse selectMember(Long memberId) {
        Member selectedMember = findById(memberId);
        return new SelectMemberResponse(selectedMember.getAlias(), selectedMember.getName(), selectedMember.getEmail(), selectedMember.getPhone());
    }

    @Transactional
    public ChangePasswordResponse changePassword(Long memberId, ChangePasswordRequest changePasswordRequest) {
        Member member = findById(memberId);
        member.changePassword(changePasswordRequest.getPassword());
        return new ChangePasswordResponse("비밀번호 변경 성공하였습니다.");
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.info("회원 조회 단계에서 존재하지 않는 회원 식별자 memberId : {}", memberId);
                    throw new NoSuchElementException("찾으려는 회원이 없습니다");
                });
    }

    public DeleteMemberResponseDto deleteMember(Long memberId) {
        Member member = findById(memberId);
        memberRepository.delete(member);
        return new DeleteMemberResponseDto("회원이 성공적으로 삭제되었습니다");
    }

    public DeleteMemberResponseDto deleteMember2(long memberId) {
        memberRepository.deleteById(memberId);
        return new DeleteMemberResponseDto("회원이 성공적으로 삭제되었습니다");
    }
}
