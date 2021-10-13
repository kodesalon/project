package com.project.kodesalon.service.member;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.image.Image;
import com.project.kodesalon.domain.member.Member;
import com.project.kodesalon.domain.member.vo.Alias;
import com.project.kodesalon.repository.board.BoardRepository;
import com.project.kodesalon.repository.member.MemberRepository;
import com.project.kodesalon.service.dto.request.MemberChangePasswordRequest;
import com.project.kodesalon.service.dto.request.MemberCreateRequest;
import com.project.kodesalon.service.dto.request.MemberDeleteRequest;
import com.project.kodesalon.service.dto.response.MemberSelectResponse;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.project.kodesalon.exception.ErrorCode.ALREADY_EXIST_MEMBER_ALIAS;
import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_MEMBER;
import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_MEMBER_ALIAS;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private Member member;

    @Mock
    private Board board;

    @Mock
    private Image image;

    @Test
    @DisplayName("회원가입이 성공하면 DB에 회원 객체를 저장합니다.")
    void join() {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", "Password123!!", "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());
        given(member.getId()).willReturn(1L);
        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);

        memberService.join(memberCreateRequest);

        verify(memberRepository, times(1)).findMemberByAlias(any(Alias.class));
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 아이디(Alias)일 경우, 예외 메세지를 반환합니다.")
    void join_throw_exception_with_already_exist() {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", "Password123!!", "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());
        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.of(member));

        thenThrownBy(() -> memberService.join(memberCreateRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ALREADY_EXIST_MEMBER_ALIAS);
    }

    @Test
    @DisplayName("회원 가입시 삭제한 회원이 다시 가입했을 경우에는 예외를 발생시킵니다")
    void join_throw_exception_with_left_alias_after_delete() {
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest("alias", "Password123!!", "이름", "email@email.com", "010-1234-5678", LocalDateTime.now());
        given(memberRepository.save(any(Member.class))).willThrow(new DataIntegrityViolationException(ALREADY_EXIST_MEMBER_ALIAS));

        thenThrownBy(() -> memberService.join(memberCreateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ALREADY_EXIST_MEMBER_ALIAS);
    }

    @Test
    @DisplayName("회원정보 조회 성공 시, 회원 별명, 이름, 이메일, 전화 번호, 회원이 올린 게시물들을 반환합니다.")
    void exist_id_response_member() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(member.getAlias()).willReturn("alias");
        given(member.getName()).willReturn("이름");
        given(member.getEmail()).willReturn("email@email.com");
        given(member.getPhone()).willReturn("010-1111-2222");
        given(boardRepository.selectMyBoards(anyLong(), anyLong(), anyInt()))
                .willReturn(List.of(board));
        given(board.getId()).willReturn(1L);
        given(board.getTitle()).willReturn("게시물 제목");
        given(board.getContent()).willReturn("게시물 내용");
        given(board.getImages()).willReturn(List.of(image));
        given(image.getId()).willReturn(1L);
        given(image.getUrl()).willReturn("localhost:8080/image.jpg");

        MemberSelectResponse memberSelectResponse = memberService.selectMember(1L, 10L, 1);

        softly.then(memberSelectResponse.getAlias()).isEqualTo("alias");
        softly.then(memberSelectResponse.getName()).isEqualTo("이름");
        softly.then(memberSelectResponse.getEmail()).isEqualTo("email@email.com");
        softly.then(memberSelectResponse.getPhone()).isEqualTo("010-1111-2222");
        softly.then(memberSelectResponse.getOwnBoards().getBoards()).isNotEmpty();
        softly.assertAll();
    }

    @Test
    @DisplayName("회원 정보 조회 시 찾으려는 회원이 없으면 예외를 반환합니다.")
    void select_not_exist_id_throws_exception() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> memberService.selectMember(1L, 1L, 1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NOT_EXIST_MEMBER);
    }

    @Test
    @DisplayName("비밀번호를 변경한다.")
    void changePassword() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        MemberChangePasswordRequest memberChangePasswordRequest = new MemberChangePasswordRequest("ChangePassword1!", LocalDateTime.now());
        memberService.changePassword(anyLong(), memberChangePasswordRequest);
        verify(member, times(1)).changePassword(anyString(), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("회원 탈퇴에 성공한다.")
    void deleteMember() {
        MemberDeleteRequest memberDeleteRequest = new MemberDeleteRequest(LocalDateTime.now());
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        memberService.deleteMember(member.getId(), memberDeleteRequest);

        verify(boardRepository, times(1)).deleteBoardByMemberId(anyLong());
        verify(member, times(1)).delete(any(LocalDateTime.class));
    }

    @Test
    @DisplayName("회원 탈퇴시, 존재하지 않는 회원 식별 번호면 예외를 발생시킨다.")
    void deleteMember_throws_exception() {
        MemberDeleteRequest memberDeleteRequest = new MemberDeleteRequest(LocalDateTime.now());
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> memberService.deleteMember(member.getId(), memberDeleteRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NOT_EXIST_MEMBER);
    }

    @Test
    @DisplayName("회원 식별 번호를 인자로 받아, 해당 식별 번호를 가진 회원을 DB로 조회한다.")
    void findById() {
        Member member = mock(Member.class);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        Member foundMember = memberService.findById(anyLong());

        then(foundMember).isEqualTo(foundMember);
    }

    @Test
    @DisplayName("회원 식별 번호가 존재하지 않는 경우 예외를 발생시킨다.")
    void findById_throw_exception_with_no_member() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> memberService.findById(anyLong()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NOT_EXIST_MEMBER);
    }

    @Test
    @DisplayName("회원 아이디로 조회시 회원이 없으면 예외를 발생시킨다.")
    void findByAlias_throw_exception_with_not_exist_member() {
        given(memberRepository.findMemberByAlias(any(Alias.class)))
                .willReturn(Optional.empty());

        thenThrownBy(() -> memberService.findMemberByAlias("alias")).isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(NOT_EXIST_MEMBER_ALIAS);
    }
}
