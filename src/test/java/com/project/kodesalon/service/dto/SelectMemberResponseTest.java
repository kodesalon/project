package com.project.kodesalon.service.dto;


import com.project.kodesalon.service.dto.response.SelectMemberResponse;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SelectMemberResponseTest {
    @Test
    @DisplayName("생성자를 초기화 하면 필드가 초기화됩니다.")
    void create_constructor_init_filed() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        SelectMemberResponse selectMemberResponse = new SelectMemberResponse("alias", "이름", "email@email.com", "010-1111-2222");

        softly.then(selectMemberResponse.getAlias()).isEqualTo("alias");
        softly.then(selectMemberResponse.getName()).isEqualTo("이름");
        softly.then(selectMemberResponse.getEmail()).isEqualTo("email@email.com");
        softly.then(selectMemberResponse.getPhone()).isEqualTo("010-1111-2222");
        softly.assertAll();
    }
}
