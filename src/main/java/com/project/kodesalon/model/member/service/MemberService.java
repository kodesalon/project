package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.controller.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.controller.dto.LoginRequest;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.repository.MemberRepository;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String alias = loginRequest.getAlias();
        Member member = findMemberByAlias(alias);
        String password = loginRequest.getPassword();
        member.login(password);
        log.info("ID : {}, Alias : {} Member 로그인", member.getId(), member.getAlias());
        return new LoginResponse(member.getId(), member.getAlias());
    }

    private Member findMemberByAlias(String alias) {
        return memberRepository.findMemberByAlias(new Alias(alias))
                .orElseThrow(() -> {
                    log.info("{}인 Alias를 가진 사용자가 존재하지 않음", alias);
                    throw new NoSuchElementException("존재하는 아이디를 입력해주세요.");
                });
    }

    public LoginResponse join(CreateMemberRequest createMemberRequest) {
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
                    throw new IllegalStateException("이미 존재하는 아이디입니다");
                });
    }
}
