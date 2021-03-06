package com.project.kodesalon.service.member;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.member.Member;
import com.project.kodesalon.domain.member.vo.Alias;
import com.project.kodesalon.repository.board.BoardRepository;
import com.project.kodesalon.repository.member.MemberRepository;
import com.project.kodesalon.service.dto.request.LoginRequest;
import com.project.kodesalon.service.dto.request.MemberChangePasswordRequest;
import com.project.kodesalon.service.dto.request.MemberCreateRequest;
import com.project.kodesalon.service.dto.request.MemberDeleteRequest;
import com.project.kodesalon.service.dto.response.MemberSelectResponse;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockHttpSession;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.project.kodesalon.exception.ErrorCode.ALREADY_EXIST_MEMBER_ALIAS;
import static com.project.kodesalon.exception.ErrorCode.INCORRECT_PASSWORD;
import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_MEMBER;
import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_MEMBER_ALIAS;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    private final MemberCreateRequest memberCreateRequest =
            new MemberCreateRequest("alias", "Password123!!", "??????", "email@email.com", "010-1111-2222", LocalDateTime.now());

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private Member member;

    @Test
    @DisplayName("??????????????? ???????????? DB??? ?????? ????????? ???????????????.")
    void join() {
        given(member.getId()).willReturn(1L);
        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);

        memberService.join(memberCreateRequest);

        verify(memberRepository, times(1)).findMemberByAlias(any(Alias.class));
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("???????????? ??? ?????? ???????????? ?????????(Alias)??? ??????, ?????? ???????????? ???????????????.")
    void join_throw_exception_with_already_exist() {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", "Password123!!", "??????", "email@email.com", "010-1234-5678", LocalDateTime.now());
        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.of(member));

        thenThrownBy(() -> memberService.join(memberCreateRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ALREADY_EXIST_MEMBER_ALIAS);
    }

    @Test
    @DisplayName("?????? ????????? ????????? ????????? ?????? ???????????? ???????????? ????????? ??????????????????")
    void join_throw_exception_with_left_alias_after_delete() {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", "Password123!!", "??????", "email@email.com", "010-1234-5678", LocalDateTime.now());
        given(memberRepository.save(any(Member.class))).willThrow(new DataIntegrityViolationException(ALREADY_EXIST_MEMBER_ALIAS));

        thenThrownBy(() -> memberService.join(memberCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ALREADY_EXIST_MEMBER_ALIAS);
    }

    @Test
    @DisplayName("?????? ??????, ??????????????? ???????????? ?????? alias??? DTO??? ?????? ????????????.")
    void login() {
        Member member = mock(Member.class);
        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.of(member));
        HttpSession session = new MockHttpSession();
        memberService.login(new LoginRequest("alias", "Password123!"), session);

        verify(memberRepository, times(1)).findMemberByAlias(any(Alias.class));
    }

    @Test
    @DisplayName("????????? ??? ???????????? ?????? ?????????(Alias)??? ??????, ????????? ???????????????.")
    void login_throw_exception_with_invalid_alias() {
        LoginRequest loginRequest = new LoginRequest("alias", "Password123!!");
        given(memberRepository.findMemberByAlias(any(Alias.class))).willThrow(new NoSuchElementException(NOT_EXIST_MEMBER_ALIAS));
        MockHttpSession session = new MockHttpSession();

        thenThrownBy(() -> memberService.login(loginRequest, session))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(NOT_EXIST_MEMBER_ALIAS);
    }

    @Test
    @DisplayName("????????? ??? ???????????? ????????? ??????, ?????? ???????????? ???????????????.")
    void login_throw_exception_with_invalid_password() {
        LoginRequest loginRequest = new LoginRequest("alias", "Password123!!");
        given(memberRepository.findMemberByAlias(any(Alias.class))).willThrow(new IllegalArgumentException(INCORRECT_PASSWORD));
        MockHttpSession session = new MockHttpSession();

        thenThrownBy(() -> memberService.login(loginRequest, session))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INCORRECT_PASSWORD);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,false", "10,true"})
    @DisplayName("???????????? ?????? ?????? ???, ?????? ??????, ??????, ?????????, ?????? ??????, ????????? ?????? ???????????? ????????? ????????? ???????????????.")
    void exist_id_response_member(int size, boolean expected) {
        given(member.getAlias()).willReturn("alias");
        given(member.getName()).willReturn("??????");
        given(member.getEmail()).willReturn("email@email.com");
        given(member.getPhone()).willReturn("010-1111-2222");
        Board board = new Board("title", "content", member, LocalDateTime.now());
        List<Board> boards = Arrays.asList(board, board);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(boardRepository.selectMyBoards(anyLong(), anyLong(), anyLong())).willReturn(boards);

        MemberSelectResponse memberSelectResponse = memberService.selectMember(1L, Long.MAX_VALUE, size);

        softly.then(memberSelectResponse.getAlias()).isEqualTo("alias");
        softly.then(memberSelectResponse.getName()).isEqualTo("??????");
        softly.then(memberSelectResponse.getEmail()).isEqualTo("email@email.com");
        softly.then(memberSelectResponse.getPhone()).isEqualTo("010-1111-2222");
        softly.then(memberSelectResponse.getBoards()).isNotNull();
        softly.then(memberSelectResponse.getBoards().isLast()).isEqualTo(expected);
        softly.assertAll();
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??? ???????????? ????????? ????????? ????????? ???????????????.")
    void select_not_exist_id_throws_exception() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> memberService.selectMember(1L, Long.MAX_VALUE, 10))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NOT_EXIST_MEMBER);
    }

    @Test
    @DisplayName("??????????????? ????????????.")
    void changePassword() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        MemberChangePasswordRequest memberChangePasswordRequest = new MemberChangePasswordRequest("ChangePassword1!", LocalDateTime.now());
        memberService.changePassword(anyLong(), memberChangePasswordRequest);
        verify(member, times(1)).changePassword(anyString(), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("?????? ????????? ????????????.")
    void deleteMember() {
        MemberDeleteRequest memberDeleteRequest = new MemberDeleteRequest(LocalDateTime.now());
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        memberService.deleteMember(member.getId(), memberDeleteRequest);

        verify(boardRepository, times(1)).deleteBoardByMemberId(anyLong());
        verify(member, times(1)).delete(any(LocalDateTime.class));
    }

    @Test
    @DisplayName("?????? ?????????, ???????????? ?????? ?????? ?????? ????????? ????????? ???????????????.")
    void deleteMember_throws_exception() {
        MemberDeleteRequest memberDeleteRequest = new MemberDeleteRequest(LocalDateTime.now());
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> memberService.deleteMember(member.getId(), memberDeleteRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NOT_EXIST_MEMBER);
    }

    @Test
    @DisplayName("?????? ?????? ????????? ????????? ??????, ?????? ?????? ????????? ?????? ????????? DB??? ????????????.")
    void findById() {
        Member member = mock(Member.class);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        Member foundMember = memberService.findById(anyLong());

        then(foundMember).isEqualTo(foundMember);
    }

    @Test
    @DisplayName("?????? ?????? ????????? ???????????? ?????? ?????? ????????? ???????????????.")
    void findById_throw_exception_with_no_member() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> memberService.findById(anyLong()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NOT_EXIST_MEMBER);
    }
}
