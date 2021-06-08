package com.project.kodesalon.model.member.controller.dto;

import com.project.kodesalon.model.member.service.dto.CreateMemberRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateMemberRequest {
    private String alias;
    private String password;
    private String name;
    private String email;
    private String phone;

    public CreateMemberRequest(String alias, String password, String name, String email, String phone) {
        this.alias = alias;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public CreateMemberRequestDto toCreateMemberRequestDto() {
        return new CreateMemberRequestDto(alias, password, name, email, phone);
    }
}
