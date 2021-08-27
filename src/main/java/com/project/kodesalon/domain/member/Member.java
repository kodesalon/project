package com.project.kodesalon.domain.member;

import com.project.kodesalon.domain.BaseEntity;
import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.member.vo.Alias;
import com.project.kodesalon.domain.member.vo.Email;
import com.project.kodesalon.domain.member.vo.Name;
import com.project.kodesalon.domain.member.vo.Password;
import com.project.kodesalon.domain.member.vo.Phone;
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
import java.util.Optional;

import static com.project.kodesalon.exception.ErrorCode.DUPLICATED_PASSWORD;
import static com.project.kodesalon.exception.ErrorCode.INVALID_DATE_TIME;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_PASSWORD;

@Slf4j
@Entity
@Where(clause = "deleted = 'false'")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(name = "member_unique_constraint", columnNames = {"alias"})})
public class Member extends BaseEntity {

    private static final String PHONE_EMPTY = "";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(nullable = false, name = "deleted", columnDefinition = "boolean default false")
    private boolean deleted;

    public Member(final String alias, final String password, final String name, final String email, final String phone, final LocalDateTime createdDateTime) {
        this.alias = new Alias(alias);
        this.password = new Password(password);
        this.email = new Email(email);
        this.name = new Name(name);
        this.createdDateTime = createdDateTime;
        if (phone != null) {
            this.phone = new Phone(phone);
        }
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
        return Optional.ofNullable(phone)
                .map(Phone::value)
                .orElse(PHONE_EMPTY);
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
        validateDateTime(lastModifiedDateTime);
        this.password = newPassword;
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    private void validateDuplication(final Password newPassword) {
        if (hasSamePassword(newPassword)) {
            throw new IllegalArgumentException(DUPLICATED_PASSWORD);
        }
    }

    private void validateDateTime(final LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            throw new IllegalArgumentException(INVALID_DATE_TIME);
        }
    }

    public void delete(final LocalDateTime deletedDateTime) {
        validateDateTime(deletedDateTime);
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
