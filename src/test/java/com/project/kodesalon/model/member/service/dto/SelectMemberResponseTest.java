package com.project.kodesalon.model.member.service.dto;


import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class SelectMemberResponseTest {

    @Test
    @DisplayName("생성자를 초기화 하면 필드가 초기화됩니다.")
    void create_constructor_init_filed() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        List<SelectMemberOwnBoardResponse> ownBoard
                = Collections.singletonList(new SelectMemberOwnBoardResponse(1L, "게시물 제목", "게시물 내용", LocalDateTime.now()));
        SelectMemberResponse selectMemberResponse = new SelectMemberResponse("alias", "이름", "email@email.com", "010-1111-2222", ownBoard);

        softly.then(selectMemberResponse.getAlias()).isEqualTo("alias");
        softly.then(selectMemberResponse.getName()).isEqualTo("이름");
        softly.then(selectMemberResponse.getEmail()).isEqualTo("email@email.com");
        softly.then(selectMemberResponse.getPhone()).isEqualTo("010-1111-2222");
        softly.then(selectMemberResponse.getOwnBoards().size()).isEqualTo(1);
        softly.assertAll();
    }
}
