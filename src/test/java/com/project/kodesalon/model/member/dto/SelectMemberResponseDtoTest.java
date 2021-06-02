package com.project.kodesalon.model.member.dto;


import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Phone;
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
                () -> then(selectMemberResponseDto.getAlias()).isEqualTo(new Alias("alias")),
                () -> then(selectMemberResponseDto.getName()).isEqualTo(new Name("이름")),
                () -> then(selectMemberResponseDto.getEmail()).isEqualTo(new Email("email@email.com")),
                () -> then(selectMemberResponseDto.getPhone()).isEqualTo(new Phone("010-1111-2222"))
        );
    }
}
