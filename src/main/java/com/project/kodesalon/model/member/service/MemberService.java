package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.dto.CreateMemberRequestDto;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.repository.MemberRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findMemberByAlias(loginRequestDto.getAlias())
                .orElseThrow(() -> HttpClientErrorException.create("존재하는 아이디를 입력해주세요.", HttpStatus.UNAUTHORIZED,
                        "", HttpHeaders.EMPTY, null, null));

        if (member.isIncorrectPassword(loginRequestDto.getPassword())) {
            throw HttpClientErrorException.create("비밀 번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED,
                    "", HttpHeaders.EMPTY, null, null);
        }

        return new LoginResponseDto(member.getId(), member.getAlias());
    }

    public LoginResponseDto join(CreateMemberRequestDto createMemberRequestDto) {
        memberRepository.findMemberByAlias(createMemberRequestDto.getAlias())
                .ifPresent(member -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다");
                });

        Member savedMember = memberRepository.save(new Member(createMemberRequestDto.getAlias().value(), createMemberRequestDto.getPassword().value(),
                createMemberRequestDto.getName().value(), createMemberRequestDto.getEmail().value(), createMemberRequestDto.getPhone().value()));

        return new LoginResponseDto(savedMember.getId(), savedMember.getAlias());
    }
}
