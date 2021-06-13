package com.project.kodesalon.model.member.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Phone {
    public static final String PHONE_REGEX = "^01(?:0|1|[6-9])[-](\\d{3}|\\d{4})[-](\\d{4})";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    public static final String PHONE_EXCEPTION_MESSAGE = "휴대폰 번호는 [3자리 수] - [3 ~ 4자리 수] - [4자리 수]의 형식 이어야 합니다.";

    @Column(name = "phone", length = 20)
    private String phone;

    public Phone(final String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException(PHONE_EXCEPTION_MESSAGE);
        }

        this.phone = phone;
    }

    public String value() {
        return phone;
    }
}
