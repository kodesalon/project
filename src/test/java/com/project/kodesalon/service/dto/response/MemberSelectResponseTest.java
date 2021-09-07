package com.project.kodesalon.service.dto.response;


import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class MemberSelectResponseTest {

    @ParameterizedTest
    @CsvSource(value = {"1,false", "10,true"})
    @DisplayName("생성자를 초기화 하면 필드가 초기화됩니다.")
    void create_constructor_init_filed(int size, boolean expected) {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        List<BoardImageResponse> boardImages = Collections.singletonList(new BoardImageResponse(1L, "localhost:8080/bucket/directory/image.jpeg"));
        List<BoardSelectResponse> content = Arrays.asList(new BoardSelectResponse(1L, "title", "content", LocalDateTime.now(), 1L, "alias", boardImages),
                new BoardSelectResponse(2L, "title", "content", LocalDateTime.now(), 1L, "alias", boardImages));
        MultiBoardSelectResponse multiBoardSelectResponse = new MultiBoardSelectResponse(content, size);
        MemberSelectResponse memberSelectResponse
                = new MemberSelectResponse("alias", "이름", "email@email.com", "010-1111-2222", multiBoardSelectResponse);

        softly.then(memberSelectResponse.getAlias()).isEqualTo("alias");
        softly.then(memberSelectResponse.getName()).isEqualTo("이름");
        softly.then(memberSelectResponse.getEmail()).isEqualTo("email@email.com");
        softly.then(memberSelectResponse.getPhone()).isEqualTo("010-1111-2222");
        softly.then(memberSelectResponse.getBoards().getBoards()).isNotEmpty();
        softly.then(memberSelectResponse.getBoards().isLast()).isEqualTo(expected);
        softly.assertAll();
    }
}
