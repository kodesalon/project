package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.dto.CreateMemberRequestDto;
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
        Member member = memberRepository.findMemberByAlias(loginRequestDto.getAlias())
                .orElseThrow(() -> new UnAuthorizedException("존재하는 아이디를 입력해주세요."));

        if (member.isIncorrectPassword(loginRequestDto.getPassword())) {
            throw new UnAuthorizedException("비밀 번호가 일치하지 않습니다.");
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
