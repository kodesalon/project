package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MemberService {
    private static final String NO_MEMBER_ELEMENT_EXCEPTION_MESSAGE = "존재하는 Alias를 입력해주세요.";
    private static final String PASSWORD_NOT_MATCH_EXCEPTION_MESSAGE = "일치하는 비밀번호를 입력해주세요.";

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findMemberByAlias(new Alias(loginRequestDto.getAlias()))
                .orElseThrow(() -> new NoSuchElementException(NO_MEMBER_ELEMENT_EXCEPTION_MESSAGE));

        if (member.isIncorrectPassword(loginRequestDto.getPassword())) {
            return new ResponseEntity<>(new LoginResponseDto(PASSWORD_NOT_MATCH_EXCEPTION_MESSAGE), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(new LoginResponseDto(member.getId(), member.getAlias()), HttpStatus.OK);
    }
}
