package com.project.kodesalon.model.board.domain;

import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import com.project.kodesalon.model.member.domain.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private boolean isDeleted;

    public Board(Title title, Content content, Member writer, LocalDateTime createdDateTime) {
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

    public boolean isDeleted() {
        return isDeleted;
    }
}

