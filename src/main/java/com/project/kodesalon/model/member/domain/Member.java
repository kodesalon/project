package com.project.kodesalon.model.member.domain;

import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.domain.vo.Phone;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(name = "member_unique_constraint", columnNames = {"alias"})})
@Where(clause = "deleted = 'false'")
@Slf4j
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

    @Column(name = "deleted")
    private boolean deleted = false;

    public Member(final String alias, final String password, final String name, final String email, final String phone) {
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
        return alias.value();
    }

    public String getPassword() {
        return password.value();
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

    public boolean isDeleted() {
        return deleted;
    }

    public boolean hasSamePassword(final Password password) {
        return this.password.equals(password);
    }

    public void login(final String password) {
        Password inputPassword = new Password(password);

        if (!hasSamePassword(inputPassword)) {
            log.info("{}의 Password가 일치하지 않음", getAlias());
            throw new IllegalArgumentException("비밀 번호가 일치하지 않습니다.");
        }
    }

    public void changePassword(final String password) {
        final Password newPassword = new Password(password);

        if (hasSamePassword(newPassword)) {
            throw new IllegalArgumentException("변경하려는 패스워드가 기존 패스워드와 일치합니다.");
        }

        this.password = newPassword;
    }

    public void delete() {
        deleted = true;
    }
}
