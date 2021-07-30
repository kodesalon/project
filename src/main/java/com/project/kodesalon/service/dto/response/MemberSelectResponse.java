package com.project.kodesalon.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberSelectResponse {
    private String alias;
    private String name;
    private String email;
    private String phone;

    public MemberSelectResponse(final String alias, final String name, final String email, final String phone) {
        this.alias = alias;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
