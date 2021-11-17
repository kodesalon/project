package com.project.kodesalon.service.dto.response;

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
}
