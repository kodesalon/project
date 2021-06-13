package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.controller.dto.LoginRequest;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.repository.MemberRepository;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequestDto;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

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

    public LoginResponse join(CreateMemberRequestDto createMemberRequestDto) {
        memberRepository.findMemberByAlias(createMemberRequestDto.getAlias())
                .ifPresent(member -> {
                    log.info("{}는 이미 존재하는 Alias입니다.", createMemberRequestDto.getAlias().value());
                    throw new IllegalStateException("이미 존재하는 아이디입니다");
                });

        Member saveMember = memberRepository.save(createMemberRequestDto.toMember());

        log.info("ID : {}, Alias : {} Member가 회원 가입 성공", saveMember.getId(), saveMember.getAlias());
        return new LoginResponse(saveMember.getId(), saveMember.getAlias());
    }
}
