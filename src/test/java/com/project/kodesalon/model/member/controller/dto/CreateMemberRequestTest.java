package com.project.kodesalon.model.member.controller.dto;

import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.domain.vo.Phone;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequestDto;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreateMemberRequestTest {
    private final CreateMemberRequest createMemberRequest = new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", "010-1111-2222");
    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    @Test
    @DisplayName("생성자를 호출하면 필드를 초기화합니다.")
    void create_init_field() {
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
        CreateMemberRequestDto createMemberRequestDto = createMemberRequest.toCreateMemberRequestDto();

        softly.then(createMemberRequestDto.getAlias()).isEqualTo(new Alias("alias"));
        softly.then(createMemberRequestDto.getPassword()).isEqualTo(new Password("Password123!!"));
        softly.then(createMemberRequestDto.getName()).isEqualTo(new Name("이름"));
        softly.then(createMemberRequestDto.getEmail()).isEqualTo(new Email("email@email.com"));
        softly.then(createMemberRequestDto.getPhone()).isEqualTo(new Phone("010-1111-2222"));

        softly.assertAll();
    }
}
