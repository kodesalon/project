package com.project.kodesalon.model.member.service.dto;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.domain.vo.Phone;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreateMemberRequestDtoTest {
    private CreateMemberRequestDto createMemberRequestDto;

    @BeforeEach
    void setUp() {
        createMemberRequestDto = new CreateMemberRequestDto("alias",
                "Password123!!", "이름", "email@email.com", "010-1111-2222");
    }

    @Test
    @DisplayName("CreateMemberDto를 생성하면 필드를 초기화 합니다.")
    void create_create_member_dto_init_field() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        softly.then(createMemberRequestDto.getAlias()).isEqualTo(new Alias("alias"));
        softly.then(createMemberRequestDto.getPassword()).isEqualTo(new Password("Password123!!"));
        softly.then(createMemberRequestDto.getName()).isEqualTo(new Name("이름"));
        softly.then(createMemberRequestDto.getEmail()).isEqualTo(new Email("email@email.com"));
        softly.then(createMemberRequestDto.getPhone()).isEqualTo(new Phone("010-1111-2222"));

        softly.assertAll();
    }

    @Test
    @DisplayName("toMember을 호출하면 Member객체를 생성해서 리턴합니다")
    void to_member_return_member() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        Member member = createMemberRequestDto.toMember();

        softly.then(member.getAlias()).isEqualTo("alias");
        softly.then(member.getPassword()).isEqualTo("Password123!!");
        softly.then(member.getName()).isEqualTo("이름");
        softly.then(member.getEmail()).isEqualTo("email@email.com");
        softly.then(member.getPhone()).isEqualTo("010-1111-2222");

        softly.assertAll();
    }
}
