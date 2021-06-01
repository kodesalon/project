package com.project.kodesalon.model.member.dto;

import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.domain.vo.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CreateMemberRequestDtoTest {
    private CreateMemberRequestDto createMemberRequestDto;

    @BeforeEach
    void setUp() {
        createMemberRequestDto = new CreateMemberRequestDto("alias",
                "Password123!!", "이름", "eamil@email.com", "010-1111-2222");
    }

    @Test
    @DisplayName("CreateMemberDto를 생성하면 필드를 초기화 합니다.")
    void create_create_member_dto_init_field() {
        assertAll(
                () -> then(createMemberRequestDto.getAlias()).isEqualTo(new Alias("alias")),
                () -> then(createMemberRequestDto.getPassword()).isEqualTo(new Password("Password123!!")),
                () -> then(createMemberRequestDto.getName()).isEqualTo(new Name("이름")),
                () -> then(createMemberRequestDto.getEmail()).isEqualTo(new Email("eamil@email.com")),
                () -> then(createMemberRequestDto.getPhone()).isEqualTo(new Phone("010-1111-2222"))
        );
    }
}
