package com.project.kodesalon.model.member.service.dto;

import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Phone;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SelectMemberResponse {
    private Alias alias;
    private Name name;
    private Email email;
    private Phone phone;

    public SelectMemberResponse(String alias, String name, String email, String phone) {
        this.alias = new Alias(alias);
        this.name = new Name(name);
        this.email = new Email(email);
        this.phone = new Phone(phone);
    }

    public String getAlias() {
        return alias.value();
    }

    public String getName() {
        return name.value();
    }

    public String getEmail() {
        return email.value();
    }

    public String getPhone() {
        return phone.value();
    }
}
