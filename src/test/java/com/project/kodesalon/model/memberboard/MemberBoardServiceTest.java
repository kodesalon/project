package com.project.kodesalon.model.memberboard;

import com.project.kodesalon.model.board.repository.BoardRepository;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_MEMBER;
import static com.project.kodesalon.model.member.domain.MemberTest.TEST_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberBoardServiceTest {

    @InjectMocks
    private MemberBoardService memberBoardService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @Test
    @DisplayName("회원 식별 번호를 인자로 받아, 해당 식별 번호를 가진 회원을 DB로 조회한다.")
    void findById() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(TEST_MEMBER));

        Member member = memberBoardService.findById(anyLong());

        then(member).isEqualTo(TEST_MEMBER);
    }

    @Test
    @DisplayName("회원 식별 번호가 존재하지 않는 경우 예외를 발생시킨다.")
    void findById_throw_exception_with_no_member() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> memberBoardService.findById(anyLong()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NOT_EXIST_MEMBER);
    }

    @Test
    @DisplayName("회원 식별 번호를 인자로 받아, 해당 식별 번호의 작성자가 작성한 게시물의 삭제 flag를 true로 변경한다,")
    void deleteBoardByMemberId() {
        memberBoardService.deleteBoardByMemberId(anyLong());

        verify(boardRepository, times(1)).deleteBoardByMemberId(anyLong());
    }
}
