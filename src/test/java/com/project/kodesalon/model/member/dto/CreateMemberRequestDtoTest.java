package com.project.kodesalon.model.member.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CreateMemberRequestDtoTest {
    private static final String ALIAS = "alias";
    private static final String PASSWORD = "Password123!!";
    private static final String NAME = "이름";
    private static final String EMAIL = "eamil@email.com";
    private static final String PHONE = "010-1111-2222";

    private CreateMemberRequestDto createMemberRequestDto;

    @BeforeEach
    void setUp() {
        createMemberRequestDto = new CreateMemberRequestDto(ALIAS,
                PASSWORD, NAME, EMAIL, PHONE);
    }

    @Test
    @DisplayName("CreateMemberDto를 생성하면 필드를 초기화 합니다.")
    void create_create_member_dto_init_field() {
        assertAll(
                () -> then(createMemberRequestDto.getAlias()).isEqualTo(ALIAS),
                () -> then(createMemberRequestDto.getPassword()).isEqualTo(PASSWORD),
                () -> then(createMemberRequestDto.getName()).isEqualTo(NAME),
                () -> then(createMemberRequestDto.getEmail()).isEqualTo(EMAIL),
                () -> then(createMemberRequestDto.getPhone()).isEqualTo(PHONE)
        );
    }
}
