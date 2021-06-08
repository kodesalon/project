package com.project.kodesalon.model.member.controller;

import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.domain.vo.Phone;
import com.project.kodesalon.model.member.dto.CreateMemberRequestDto;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreateMemberRequestTest {
    private CreateMemberRequest createMemberRequest;

    @BeforeEach
    void setUp() {
        createMemberRequest = new CreateMemberRequest("alias", "Password123!!", "이름", "example@example.com", "010-1111-2222");
    }

    @Test
    @DisplayName("생성자를 호출하면 필드를 초기화합니다.")
    void create_init_field() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        softly.then(createMemberRequest.getAlias()).isEqualTo("alias");
        softly.then(createMemberRequest.getPassword()).isEqualTo("Password123!!");
        softly.then(createMemberRequest.getName()).isEqualTo("이름");
        softly.then(createMemberRequest.getEmail()).isEqualTo("email@email.com");
        softly.then(createMemberRequest.getPhone()).isEqualTo("010-1111-2222");

        softly.assertAll();
    }

    @Test
    @DisplayName("toCreateMemberRequestDto를 호출하면 createMemberRequestDto를 반환합니다")
    void to_create_member_request_dto_return_create_member_request_dto() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        CreateMemberRequestDto createMemberRequestDto = createMemberRequest.toCreateMemberRequestDto();

        softly.then(createMemberRequestDto.getAlias()).isEqualTo(new Alias("alias"));
        softly.then(createMemberRequestDto.getPassword()).isEqualTo(new Password("Password123!!"));
        softly.then(createMemberRequestDto.getName()).isEqualTo(new Name("이름"));
        softly.then(createMemberRequestDto.getEmail()).isEqualTo(new Email("email@email.com"));
        softly.then(createMemberRequestDto.getPhone()).isEqualTo(new Phone("010-1111-2222"));

        softly.assertAll();
    }
}
