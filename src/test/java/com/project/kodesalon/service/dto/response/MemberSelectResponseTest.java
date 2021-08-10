package com.project.kodesalon.service.dto.response;


import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

class MemberSelectResponseTest {

    @Test
    @DisplayName("생성자를 초기화 하면 필드가 초기화됩니다.")
    void create_constructor_init_filed() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        List<MemberOwnBoardSelectResponse> ownBoards
                = Collections.singletonList(new MemberOwnBoardSelectResponse(1L, "게시물 제목", "게시물 내용", LocalDateTime.now()));
        MemberSelectResponse memberSelectResponse
                = new MemberSelectResponse("alias", "이름", "email@email.com", "010-1111-2222", ownBoards);

        softly.then(memberSelectResponse.getAlias()).isEqualTo("alias");
        softly.then(memberSelectResponse.getName()).isEqualTo("이름");
        softly.then(memberSelectResponse.getEmail()).isEqualTo("email@email.com");
        softly.then(memberSelectResponse.getPhone()).isEqualTo("010-1111-2222");
        softly.then(memberSelectResponse.getOwnBoards().size()).isEqualTo(1);
        softly.assertAll();
    }
}
