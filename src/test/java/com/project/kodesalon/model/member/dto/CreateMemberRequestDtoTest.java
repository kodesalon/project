package com.project.kodesalon.model.member.dto;

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
                () -> then(createMemberRequestDto.getAlias()).isEqualTo("alias"),
                () -> then(createMemberRequestDto.getPassword()).isEqualTo("Password123!!"),
                () -> then(createMemberRequestDto.getName()).isEqualTo("이름"),
                () -> then(createMemberRequestDto.getEmail()).isEqualTo("eamil@email.com"),
                () -> then(createMemberRequestDto.getPhone()).isEqualTo("010-1111-2222")
        );
    }
}
