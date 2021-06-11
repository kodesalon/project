package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.dto.SelectMemberResponseDto;
import com.project.kodesalon.model.member.repository.MemberRepository;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequestDto;
import com.project.kodesalon.model.member.service.dto.LoginRequestDto;
import com.project.kodesalon.model.member.service.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findMemberByAlias(loginRequestDto.getAlias())
                .orElseThrow(() -> {
                            log.error("{}인 Alias를 가진 사용자가 존재하지 않음", loginRequestDto.getAlias());
                            throw HttpClientErrorException.create("존재하는 아이디를 입력해주세요.", HttpStatus.BAD_REQUEST,
                                    "", HttpHeaders.EMPTY, null, null);
                        }
                );

        if (member.isIncorrectPassword(loginRequestDto.getPassword())) {
            log.error("{}의 Password가 일치하지 않음", loginRequestDto.getAlias());
            throw HttpClientErrorException.create("비밀 번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST,
                    "", HttpHeaders.EMPTY, null, null);
        }

        log.info("ID : {} Alias : {} Member 로그안", member.getId(), member.getAlias());
        return new LoginResponseDto(member.getId(), member.getAlias());
    }

    @Transactional
    public LoginResponseDto join(CreateMemberRequestDto createMemberRequestDto) {
        memberRepository.findMemberByAlias(createMemberRequestDto.getAlias())
                .ifPresent(member -> {
                    log.error("회원 가입 단계에서 {}는 이미 존재하는 Alias입니다.", createMemberRequestDto.getAlias());
                    throw new IllegalStateException("이미 존재하는 아이디입니다");
                });

        Member saveMember = memberRepository.save(createMemberRequestDto.toMember());

        log.info("ID : {}, Alias : {} Member가 회원 가입 성공", saveMember.getId(), saveMember.getAlias());
        return new LoginResponseDto(saveMember.getId(), saveMember.getAlias());
    }

    @Transactional(readOnly = true)
    public SelectMemberResponseDto selectMember(Long memberId) {
        Member selectedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.info("회원 조회 단계에서 존재하지 않는 회원 식별자 memberId : {}", memberId);
                    throw new NoSuchElementException("찾으려는 회원이 없습니다");
                });

        return new SelectMemberResponseDto(selectedMember.getAlias(), selectedMember.getName(), selectedMember.getEmail(), selectedMember.getPhone());
    }
}
