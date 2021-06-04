package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.dto.CreateMemberRequestDto;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.dto.SelectMemberResponseDto;
import com.project.kodesalon.model.member.exception.UnAuthorizedException;
import com.project.kodesalon.model.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    private final LoginRequestDto loginRequestDto = new LoginRequestDto("alias", "Password123!!");
    private final CreateMemberRequestDto createMemberRequestDto = new CreateMemberRequestDto("alias2", "Password123!!", "이름", "email@email.com", "010-1111-2222");

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Member member;

    @Test
    @DisplayName("존재하지 않는 Alias는 예외를 발생시킵니다.")
    void not_exist_member_login_throw_exception() {
        LoginRequestDto loginRequestDto =
                new LoginRequestDto("alias", "Password123!!");

        when(memberRepository.findMemberByAlias(loginRequestDto.getAlias()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessage("존재하는 아이디를 입력해주세요.");
    }

    @Test
    @DisplayName("존재하는 Alias가 Alias와 Password가 일치하면 Id, Alias를 리턴합니다.")
    void exist_login_return_success2() {
        when(member.getId()).thenReturn(1L);
        when(member.getAlias()).thenReturn("alias");
        when(member.isIncorrectPassword(loginRequestDto.getPassword())).thenReturn(false);
        when(memberRepository.findMemberByAlias(loginRequestDto.getAlias()))
                .thenReturn(Optional.of(member));

        LoginResponseDto loginResponseDto = memberService.login(loginRequestDto);

        assertAll(
                () -> then(loginResponseDto.getMemberId()).isEqualTo(1L),
                () -> then(loginResponseDto.getAlias()).isEqualTo("alias")
        );
    }

    @Test
    @DisplayName("존재하는 Alias가 Password가 일치하지 않는다면 예외 메세지를 발생시킵니다.")
    void exist_login_return_fail2() {
        when(member.isIncorrectPassword(loginRequestDto.getPassword())).thenReturn(true);
        when(memberRepository.findMemberByAlias(loginRequestDto.getAlias()))
                .thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessage("비밀 번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 아이디이면 회원가입을 진행합니다.")
    void create_member_success() {
        when(memberRepository.findMemberByAlias(any(Alias.class))).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(member.getId()).thenReturn(1L);
        when(member.getAlias()).thenReturn("alias");

        LoginResponseDto loginResponseDto = memberService.join(createMemberRequestDto);

        assertAll(
                () -> then(loginResponseDto.getMemberId()).isEqualTo(1L),
                () -> then(loginResponseDto.getAlias()).isEqualTo("alias")
        );
    }

    @Test
    @DisplayName("이미 존재하는 Alias면 예외를 발생시킵니다.")
    void exist_alias_throws_exception() {
        when(memberRepository.findMemberByAlias(any(Alias.class))).thenReturn(Optional.of(member));

        assertThatIllegalStateException().isThrownBy(() -> memberService.join(createMemberRequestDto))
                .withMessage("이미 존재하는 아이디입니다");
    }

    @Test
    @DisplayName("존재하는 Alias면 회원 정보를 응답합니다.")
    void exist_id_response_member() {
        when(member.getAlias()).thenReturn("alias");
        when(member.getName()).thenReturn("이름");
        when(member.getEmail()).thenReturn("email@email.com");
        when(member.getPhone()).thenReturn("010-1111-2222");
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        SelectMemberResponseDto selectMemberResponseDto = memberService.selectMember(1L);

        assertAll(
                () -> then(selectMemberResponseDto.getAlias()).isEqualTo("alias"),
                () -> then(selectMemberResponseDto.getName()).isEqualTo("이름"),
                () -> then(selectMemberResponseDto.getEmail()).isEqualTo("email@email.com"),
                () -> then(selectMemberResponseDto.getPhone()).isEqualTo("010-1111-2222")
        );
    }

    @Test
    @DisplayName("존재하지 않는 회원을 조회하면 에외를 발생시킵니다.")
    void select_not_exist_id_throws_exception() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.selectMember(1L)).isInstanceOf(NoSuchElementException.class)
                .hasMessage("찾으려는 회원이 없습니다");
    }
}
