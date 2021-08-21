package com.project.kodesalon.domain.board;

import com.project.kodesalon.domain.BaseEntity;
import com.project.kodesalon.domain.board.vo.Content;
import com.project.kodesalon.domain.board.vo.Title;
import com.project.kodesalon.domain.image.Image;
import com.project.kodesalon.domain.member.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_IMAGES_SIZE;
import static com.project.kodesalon.exception.ErrorCode.NOT_AUTHORIZED_MEMBER;

@Slf4j
@Entity
@Where(clause = "deleted = 'false'")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {
    public static final int BOARD_IMAGE_LENGTH_MAX_BOUND = 5;

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

    @Column(nullable = false, name = "deleted", columnDefinition = "boolean default false")
    private boolean deleted;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    public Board(final String title, final String content, final Member writer, final LocalDateTime createdDateTime) {
        this.title = new Title(title);
        this.content = new Content(content);
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

    public Member getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public List<Image> getImages() {
        return images;
    }

    public void delete(final Long memberId, final LocalDateTime deletedDateTime) {
        validateAuthorizationOf(memberId);
        deleted = true;
        this.deletedDateTime = deletedDateTime;
    }

    public void updateTitleAndContent(final Long memberId, final Title updatedTitle, final Content updatedContent, final LocalDateTime lastModifiedDateTime) {
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

    private boolean isSameWriterId(final Long memberId) {
        return writer.getId().equals(memberId);
    }

    public void addImage(final Image image) {
        if (images.size() >= BOARD_IMAGE_LENGTH_MAX_BOUND) {
            throw new IllegalArgumentException(INVALID_BOARD_IMAGES_SIZE);
        }

        images.add(image);
    }
}
