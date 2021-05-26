package com.project.kodesalon.model.board.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Content {
    private static final String CHECK_CONTENT_IS_BLANK = "내용에 공백 아닌 1자 이상의 문자를 입력하였는지 확인해주세요.";

    @Lob
    private String content;

    public Content(String content) {
        if (content.isBlank()) {
            throw new IllegalArgumentException(CHECK_CONTENT_IS_BLANK);
        }

        this.content = content;
    }
}
