package com.project.kodesalon.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

import static com.project.kodesalon.domain.member.vo.Password.PASSWORD_REGEX;
import static com.project.kodesalon.exception.ErrorCode.INVALID_DATE_TIME;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_PASSWORD;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberChangePasswordRequest {

    @NotNull(message = INVALID_MEMBER_PASSWORD)
    @Pattern(regexp = PASSWORD_REGEX, message = INVALID_MEMBER_PASSWORD)
    private String password;

    @NotNull(message = INVALID_DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedDateTime;

    public MemberChangePasswordRequest(final String password, final LocalDateTime lastModifiedDateTime) {
        this.password = password;
        this.lastModifiedDateTime = lastModifiedDateTime;
    }
}
