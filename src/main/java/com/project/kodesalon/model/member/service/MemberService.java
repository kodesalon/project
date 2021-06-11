package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.repository.MemberRepository;
import com.project.kodesalon.model.member.service.dto.LoginRequestDto;
import com.project.kodesalon.model.member.service.dto.LoginResponseDto;
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
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하는 아이디를 입력해주세요."));

        if (member.isIncorrectPassword(loginRequestDto.getPassword())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "비밀 번호가 일치하지 않습니다.");
        }

        return new LoginResponseDto(member.getId(), member.getAlias());
    }
}
