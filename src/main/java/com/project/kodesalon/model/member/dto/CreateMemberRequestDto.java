package com.project.kodesalon.model.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateMemberRequestDto {
    private String alias;
    private String password;
    private String name;
    private String email;
    private String phone;

    public CreateMemberRequestDto(String alias, String password, String name, String email, String phone) {
        this.alias = alias;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}