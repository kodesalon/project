package com.project.kodesalon.model.member.dto;


import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SelectMemberResponseDtoTest {
    @Test
    @DisplayName("생성자를 초기화 하면 필드가 초기화됩니다.")
    void create_constructor_init_filed() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        SelectMemberResponseDto selectMemberResponseDto = new SelectMemberResponseDto("alias", "이름", "email@email.com", "010-1111-2222");

        softly.then(selectMemberResponseDto.getAlias()).isEqualTo("alias");
        softly.then(selectMemberResponseDto.getName()).isEqualTo("이름");
        softly.then(selectMemberResponseDto.getEmail()).isEqualTo("email@email.com");
        softly.then(selectMemberResponseDto.getPhone()).isEqualTo("010-1111-2222");
        softly.assertAll();
    }
}
