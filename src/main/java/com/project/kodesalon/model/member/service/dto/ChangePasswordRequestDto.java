package com.project.kodesalon.model.member.service.dto;

import com.project.kodesalon.model.member.domain.vo.Password;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangePasswordRequestDto {
    private Long memberId;
    private Password password;

    public ChangePasswordRequestDto(Long memberId, String password) {
        this.memberId = memberId;
        this.password = new Password(password);
    }

    public String getPassword() {
        return password.value();
    }
}
