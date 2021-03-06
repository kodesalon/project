package com.project.kodesalon.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.project.kodesalon.exception.ErrorCode.INVALID_DATE_TIME;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDeleteRequest {

    @NotNull(message = INVALID_DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime deletedDateTime;

    public MemberDeleteRequest(final LocalDateTime deletedDateTime) {
        this.deletedDateTime = deletedDateTime;
    }
}
