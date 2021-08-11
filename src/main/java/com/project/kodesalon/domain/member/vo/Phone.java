package com.project.kodesalon.domain.member.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_PHONE;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Phone {

    public static final String PHONE_REGEX = "^01[016-9][-](\\d{3}|\\d{4})[-](\\d{4})";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    @Column(name = "phone", length = 20)
    private String phone;

    public Phone(final String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException(INVALID_MEMBER_PHONE);
        }

        this.phone = phone;
    }

    public String value() {
        return phone;
    }
}
