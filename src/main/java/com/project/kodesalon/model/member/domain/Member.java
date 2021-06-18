package com.project.kodesalon.model.member.domain;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.domain.vo.Phone;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;

import static com.project.kodesalon.common.ErrorCode.PASSWORD_DUPLICATION;

@Entity
@NoArgsConstructor
@Table(name = "member", uniqueConstraints = {@UniqueConstraint(columnNames = {"alias"})})
@Where(clause = "deleted = 'false'")
@Slf4j
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Column(name = "member_id")
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

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    @Column(name = "deleted")
    @Getter
    private boolean deleted;

    public Member(final Alias alias, final Password password, final Name name, final Email email, final Phone phone) {
        this.alias = alias;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phone = phone;
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

    public List<Board> getBoards() {
        return boards;
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
            throw new IllegalArgumentException(PASSWORD_DUPLICATION);
        }

        this.password = newPassword;
    }

    public void delete() {
        deleted = true;
    }

    public void addBoard(Board newBoard) {
        boards.add(newBoard);
    }
}
