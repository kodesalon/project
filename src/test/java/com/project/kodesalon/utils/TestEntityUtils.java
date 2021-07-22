package com.project.kodesalon.utils;

import com.project.kodesalon.domain.Board;
import com.project.kodesalon.domain.Member;

import java.time.LocalDateTime;

public interface TestEntityUtils {

    static Member getTestMember() {
        return new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444", LocalDateTime.now());
    }

    static Board getTestBoard() {
        return new Board("게시물 제목", "게시물 내용", getTestMember(), LocalDateTime.now());
    }
}
