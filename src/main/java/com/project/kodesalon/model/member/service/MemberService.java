package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        memberRepository.findMemberByAlias(new Alias(loginRequestDto.getAlias()))
                .orElseThrow(() -> new NoSuchElementException("존재하는 Alias를 입력해 주세요."));


        return null;
    }
}
