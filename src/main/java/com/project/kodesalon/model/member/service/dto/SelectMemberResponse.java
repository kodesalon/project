package com.project.kodesalon.model.member.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SelectMemberResponse {

    private String alias;
    private String name;
    private String email;
    private String phone;

    public SelectMemberResponse(final String alias, final String name, final String email, final String phone) {
        this.alias = alias;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
