package com.project.kodesalon.service.dto.response;

import com.project.kodesalon.repository.board.query.dto.BoardFlatQueryDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardImageResponse {
    private Long imageId;
    private String imageUrl;

    public BoardImageResponse(final Long imageId, final String imageUrl) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
    }

    public BoardImageResponse(final BoardFlatQueryDto flat) {
        this.imageId = flat.getImageId();
        this.imageUrl = flat.getImageUrl();
    }
}
