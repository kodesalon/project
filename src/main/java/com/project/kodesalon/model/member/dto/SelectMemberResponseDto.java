package com.project.kodesalon.model.member.dto;

import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Phone;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SelectMemberResponseDto {
    private Alias alias;
    private Name name;
    private Email email;
    private Phone phone;

    public SelectMemberResponseDto(String alias, String name, String email, String phone) {
        this.alias = new Alias(alias);
        this.name = new Name(name);
        this.email = new Email(email);
        this.phone = new Phone(phone);
    }
}
