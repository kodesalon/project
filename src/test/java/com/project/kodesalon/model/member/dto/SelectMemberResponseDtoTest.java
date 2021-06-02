package com.project.kodesalon.model.member.dto;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SelectMemberResponseDtoTest {
    @Test
    @DisplayName("생성자를 초기화 하면 필드가 초기화됩니다.")
    void create_constructor_init_filed() {
        SelectMemberResponseDto selectMemberResponseDto = new SelectMemberResponseDto("alias", "이름", "email@email.com", "010-1111-2222");

        assertAll(
                () -> then(selectMemberResponseDto.getAlias()).isEqualTo("alias"),
                () -> then(selectMemberResponseDto.getName()).isEqualTo("이름"),
                () -> then(selectMemberResponseDto.getEmail()).isEqualTo("email@email.com"),
                () -> then(selectMemberResponseDto.getPhone()).isEqualTo("010-1111-2222")
        );
    }
}
