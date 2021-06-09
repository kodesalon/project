package com.project.kodesalon.model.member.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteMemberResponseDtoTest {
    @Test
    @DisplayName("")
    void create_delete_member_response_dto_init_field() {
        DeleteMemberResponseDto deleteMemberResponseDto = new DeleteMemberResponseDto("회원이 성공적으로 삭제되었습니다");

        then(deleteMemberResponseDto.getMessage())
                .isEqualTo("회원이 성공적으로 삭제되었습니다");
    }
}
