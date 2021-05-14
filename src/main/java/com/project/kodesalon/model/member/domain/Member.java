package com.project.kodesalon.model.member.domain;

import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Phone;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

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

    public Member(String alias, String email, String phone) {
        this.alias = new Alias(alias);
        this.email = new Email(email);
        this.phone = new Phone(phone);
    }

    public Long getId() {
        return id;
    }

    public String getAlias() {
        return alias.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPhone() {
        return phone.getValue();
    }
}
