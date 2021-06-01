package com.project.kodesalon.model.member.dto;

import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.domain.vo.Phone;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateMemberRequestDto {
    private Alias alias;
    private Password password;
    private Name name;
    private Email email;
    private Phone phone;

    public CreateMemberRequestDto(String alias, String password, String name, String email, String phone) {
        this.alias = new Alias(alias);
        this.password = new Password(password);
        this.name = new Name(name);
        this.email = new Email(email);
        this.phone = new Phone(phone);
    }
}
