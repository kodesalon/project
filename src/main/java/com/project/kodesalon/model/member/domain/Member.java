package com.project.kodesalon.model.member.domain;

import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.domain.vo.Phone;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Alias alias;

    @Embedded
    private Email email;

    @Embedded
    private Phone phone;

    @Embedded
    private Password password;

    @Embedded
    private Name name;

    public Member(String alias, String password, String name, String email, String phone) {
        this.alias = new Alias(alias);
        this.password = new Password(password);
        this.email = new Email(email);
        this.name = new Name(name);
        this.phone = new Phone(phone);
    }

    public Long getId() {
        return id;
    }

    public String getAlias() {
        return alias.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    public String getName() {
        return name.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPhone() {
        return phone.getValue();
    }

    public boolean isIncorrectPassword(String password) {
        return !this.getPassword().equals(password);
    }
}
