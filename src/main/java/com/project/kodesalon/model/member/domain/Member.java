package com.project.kodesalon.model.member.domain;

import com.project.kodesalon.common.BaseEntity;
import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Email;
import com.project.kodesalon.model.member.domain.vo.Name;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.domain.vo.Phone;
import lombok.AccessLevel;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_PASSWORD;
import static com.project.kodesalon.common.ErrorCode.PASSWORD_DUPLICATION;

@Slf4j
@Entity
@Where(clause = "deleted = 'false'")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(name = "member_unique_constraint", columnNames = {"alias"})})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
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

    @Column(nullable = false, name = "deleted", columnDefinition = "boolean default false")
    private boolean deleted;

    public Member(final String alias, final String password, final String name, final String email, final String phone, LocalDateTime createdDateTime) {
        this.alias = new Alias(alias);
        this.password = new Password(password);
        this.email = new Email(email);
        this.name = new Name(name);
        this.phone = new Phone(phone);
        this.createdDateTime = createdDateTime;
        this.lastModifiedDateTime = createdDateTime;
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
            throw new IllegalArgumentException(INVALID_MEMBER_PASSWORD);
        }
    }

    public void changePassword(final String password, final LocalDateTime lastModifiedDateTime) {
        final Password newPassword = new Password(password);
        validateDuplication(newPassword);
        this.password = newPassword;
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    private void validateDuplication(final Password newPassword) {
        if (hasSamePassword(newPassword)) {
            throw new IllegalArgumentException(PASSWORD_DUPLICATION);
        }
    }

    public void delete(final LocalDateTime deletedDateTime) {
        deleted = true;
        this.deletedDateTime = deletedDateTime;
    }

    public void addBoard(final Board newBoard) {
        boards.add(newBoard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
