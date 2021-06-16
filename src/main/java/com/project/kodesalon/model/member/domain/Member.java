package com.project.kodesalon.model.member.domain;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.domain.vo.Phone;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

@Entity
@NoArgsConstructor
@Table(name = "member", uniqueConstraints = {@UniqueConstraint(columnNames = {"alias"})})
@Where(clause = "deleted = 'false'")
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

    public Member(final String alias, final String password, final String name, final String email, final String phone) {
        this.alias = new Alias(alias);
        this.password = new Password(password);
        this.email = new Email(email);
        this.name = new Name(name);
        this.phone = new Phone(phone);
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

    public void addBoard(Board newBoard) {
        boards.add(newBoard);
    }
}
