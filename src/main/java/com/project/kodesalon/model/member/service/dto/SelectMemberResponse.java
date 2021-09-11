package com.project.kodesalon.model.member.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SelectMemberResponse {

    private String alias;
    private String name;
    private String email;
    private String phone;
    private List<SelectMemberOwnBoardResponse> ownBoards;

    public SelectMemberResponse(final String alias, final String name, final String email, final String phone, final List<SelectMemberOwnBoardResponse> ownBoards) {
        this.alias = alias;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.ownBoards = ownBoards;
    }
}
