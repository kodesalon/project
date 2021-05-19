package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Member의 아이디와 패스워드를 통해 아이디와 비밀번호가 일치하면 성공을 리턴합니다")
    void login() {

    }
}
