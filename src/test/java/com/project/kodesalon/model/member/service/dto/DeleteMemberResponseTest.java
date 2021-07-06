package com.project.kodesalon.model.member.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

public class DeleteMemberResponseTest {
    @Test
    @DisplayName("생성자를 호출하면 필드를 초기화합니다.")
    void create_delete_member_response_dto_init_field() {
        DeleteMemberResponse deleteMemberResponse = new DeleteMemberResponse("회원이 성공적으로 삭제되었습니다");

        then(deleteMemberResponse.getMessage()).isEqualTo("회원이 성공적으로 삭제되었습니다");
    }
}
