package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.dto.CreateMemberRequestDto;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.exception.UnAuthorizedException;
import com.project.kodesalon.model.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private static final String NO_MEMBER_ELEMENT_EXCEPTION_MESSAGE = "존재하는 Alias를 입력해주세요.";
    private static final String PASSWORD_NOT_MATCH_EXCEPTION_MESSAGE = "일치하는 비밀번호를 입력해주세요.";

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findMemberByAlias(new Alias(loginRequestDto.getAlias()))
                .orElseThrow(() -> new UnAuthorizedException(NO_MEMBER_ELEMENT_EXCEPTION_MESSAGE));

        if (member.isIncorrectPassword(loginRequestDto.getPassword())) {
            throw new UnAuthorizedException(PASSWORD_NOT_MATCH_EXCEPTION_MESSAGE);
        }

        return new ResponseEntity<>(new LoginResponseDto(member.getId(), member.getAlias()), HttpStatus.OK);
    }

    public ResponseEntity<LoginResponseDto> joinMember(CreateMemberRequestDto createMemberRequestDto) {
        Member member = new Member(createMemberRequestDto.getAlias(), createMemberRequestDto.getPassword(),
                createMemberRequestDto.getName(), createMemberRequestDto.getEmail(), createMemberRequestDto.getPhone());
        Member savedMember = memberRepository.save(member);

        return new ResponseEntity<>(new LoginResponseDto(savedMember.getId(), savedMember.getAlias()), HttpStatus.CREATED);
    }
}
