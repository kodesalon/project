package com.project.kodesalon.model.member.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CreateMemberDtoTest {
    private static final String ALIAS = "alias";
    private static final String PASSWORD = "Password123!!";
    private static final String NAME = "이름";
    private static final String EMAIL = "eamil@email.com";
    private static final String PHONE = "010-1111-2222";

    private CreateMemberDto createMemberDto;

    @BeforeEach
    void setUp() {
        createMemberDto = new CreateMemberDto(ALIAS,
                PASSWORD, NAME, EMAIL, PHONE);
    }

    @Test
    @DisplayName("CreateMemberDto를 생성하면 필드를 초기화 합니다.")
    void create_create_member_dto_init_field() {
        assertAll(
                () -> then(createMemberDto.getAlias()).isEqualTo(ALIAS),
                () -> then(createMemberDto.getPassword()).isEqualTo(PASSWORD),
                () -> then(createMemberDto.getName()).isEqualTo(NAME),
                () -> then(createMemberDto.getEmail()).isEqualTo(EMAIL),
                () -> then(createMemberDto.getPhone()).isEqualTo(PHONE)
        );
    }
}
