package com.project.kodesalon.model.member.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Phone {
    private static final String PHONE_ERROR_MESSAGE = "핸드폰 번호는 [휴대폰 앞자리 번호]- 3자리 혹은 4자리 수 - 4자리수의 형식 이어야 합니다.";
    private static final String PHONE_REGEX = "^01(?:0|1|[6-9])[-](\\d{3}|\\d{4})[-](\\d{4})";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    @Column(name = "phone", length = 20)
    private String phone;

    public Phone(final String phone) {
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException(PHONE_ERROR_MESSAGE);
        }

        this.phone = phone;
    }

    public String getValue() {
        return phone;
    }
}
