package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequestDto;
import com.project.kodesalon.model.member.repository.MemberRepository;
import com.project.kodesalon.model.member.service.dto.LoginRequestDto;
import com.project.kodesalon.model.member.service.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findMemberByAlias(loginRequestDto.getAlias())
                .orElseThrow(() -> {
                    log.error("로그인 단계에서 {}의 Alias가 존재하지 않음", loginRequestDto.getAlias());
                    HttpClientErrorException.create("존재하는 아이디를 입력해주세요.", HttpStatus.UNAUTHORIZED,
                            "", HttpHeaders.EMPTY, null, null);
                    return null;
                });

        if (member.isIncorrectPassword(loginRequestDto.getPassword())) {
            log.error("{}의 Password가 일치하지 않음", loginRequestDto.getAlias());
            throw HttpClientErrorException.create("비밀 번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED,
                    "", HttpHeaders.EMPTY, null, null);
        }

        log.info("ID : {} Alias : {} Member 로그안", member.getId(), member.getAlias());
        return new LoginResponseDto(member.getId(), member.getAlias());
    }

    public LoginResponseDto join(CreateMemberRequestDto createMemberRequestDto) {
        memberRepository.findMemberByAlias(createMemberRequestDto.getAlias())
                .ifPresent(member -> {
                    log.error("회원 가입 단계에서 {}는 이미 존재하는 Alias입니다.", createMemberRequestDto.getAlias());
                    throw new IllegalStateException("이미 존재하는 아이디입니다");
                });

        Member saveMember = new Member(createMemberRequestDto.getAlias().value(), createMemberRequestDto.getPassword().value(),
                createMemberRequestDto.getName().value(), createMemberRequestDto.getEmail().value(), createMemberRequestDto.getPhone().value());

        memberRepository.save(saveMember);

        log.info("ID : {}, Alias : {} Member가 회원 가입 성공", saveMember.getId(), saveMember.getAlias());
        return new LoginResponseDto(saveMember.getId(), saveMember.getAlias());
    }
}
