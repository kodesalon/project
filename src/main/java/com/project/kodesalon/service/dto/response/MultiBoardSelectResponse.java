package com.project.kodesalon.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiBoardSelectResponse {
    private List<BoardSelectResponse> boards;
    private boolean last;

    public MultiBoardSelectResponse(List<BoardSelectResponse> boards, boolean last) {
        this.boards = boards;
        this.last = last;
    }
}
