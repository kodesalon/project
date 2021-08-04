package com.project.kodesalon.model.board.domain;

import com.project.kodesalon.common.BaseEntity;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import com.project.kodesalon.model.member.domain.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static com.project.kodesalon.common.ErrorCode.NOT_AUTHORIZED_MEMBER;

@Slf4j
@Entity
@Where(clause = "deleted = 'false'")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", updatable = false)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member writer;

    @Column(nullable = false, name = "deleted", columnDefinition = "boolean default false")
    private boolean deleted;

    public Board(final String title, final String content, final Member writer, final LocalDateTime createdDateTime) {
        this.title = new Title(title);
        this.content = new Content(content);
        this.writer = writer;
        this.createdDateTime = createdDateTime;
        this.lastModifiedDateTime = createdDateTime;
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

    public Member getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete(final Long memberId, final LocalDateTime deletedDateTime) {
        validateAuthorizationOf(memberId);
        deleted = true;
        this.deletedDateTime = deletedDateTime;
    }

    public void updateTitleAndContent(Long memberId, Title updatedTitle, Content updatedContent, LocalDateTime lastModifiedDateTime) {
        validateAuthorizationOf(memberId);
        this.title = updatedTitle;
        this.content = updatedContent;
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    private void validateAuthorizationOf(final Long memberId) {
        if (!isSameWriterId(memberId)) {
            log.info("{}가 {}에 관한 권한이 없음.", memberId, id);
            throw new IllegalArgumentException(NOT_AUTHORIZED_MEMBER);
        }
    }

    private boolean isSameWriterId(Long memberId) {
        return writer.getId().equals(memberId);
    }
}

