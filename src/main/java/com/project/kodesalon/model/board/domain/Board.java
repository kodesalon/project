package com.project.kodesalon.model.board.domain;

import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import com.project.kodesalon.model.member.domain.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

import static com.project.kodesalon.common.ErrorCode.ALREADY_DELETED_BOARD;
import static com.project.kodesalon.common.ErrorCode.NOT_AUTHORIZED_MEMBER;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted = 'false'")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;

    private boolean deleted;

    public Board(final Title title, final Content content, final Member writer, final LocalDateTime createdDateTime) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdDateTime = createdDateTime;
        this.writer.addBoard(this);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title.value();
    }

    public String getContent() {
        return content.value();
    }

    public String getWriter() {
        return writer.getName();
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void updateTitleAndContent(Title updateTitle, Content updateContent) {
        this.title = updateTitle;
        this.content = updateContent;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete(final Member member) {
        validateState();
        validateAuthorizationOf(member);
        deleted = true;
    }

    private void validateState() {
        if (deleted) {
            throw new IllegalStateException(ALREADY_DELETED_BOARD);
        }
    }

    private void validateAuthorizationOf(final Member member) {
        if (!isWrittenBy(member)) {
            throw new IllegalArgumentException(NOT_AUTHORIZED_MEMBER);
        }
    }

    private boolean isWrittenBy(final Member member) {
        return writer.equals(member);
    }
}

