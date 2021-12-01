package com.project.kodesalon.domain.image;

import com.project.kodesalon.domain.board.Board;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "image", uniqueConstraints = {
        @UniqueConstraint(name = "image_unique_constraints", columnNames = {"image_url", "image_key"})
})
public class Image {

    private static final String PATH_DELIMITER = "/";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_url", nullable = false, unique = true)
    private String url;

    @Column(name = "image_key", nullable = false, unique = true)
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public Image(final String url, final Board board) {
        this.url = url;
        this.key = extractKey();
        this.board = board;
        this.board.addImage(this);
    }

    private String extractKey() {
        int indexOfImageName = url.lastIndexOf(PATH_DELIMITER);
        String urlWithoutImageName = url.substring(0, indexOfImageName);
        int indexOfDirectoryName = urlWithoutImageName.lastIndexOf(PATH_DELIMITER);

        return url.substring(indexOfDirectoryName + 1);
    }
}
