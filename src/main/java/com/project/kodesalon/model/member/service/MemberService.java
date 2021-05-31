package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.exception.UnAuthorizedException;
import com.project.kodesalon.model.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findMemberByAlias(new Alias(loginRequestDto.getAlias()))
                .orElseThrow(() -> new UnAuthorizedException("존재하는 Alias를 입력해주세요."));

        if (member.isIncorrectPassword(loginRequestDto.getPassword())) {
            throw new UnAuthorizedException("일치하는 비밀번호를 입력해주세요.");
        }

        return new LoginResponseDto(member.getId(), member.getAlias());
    }
}
