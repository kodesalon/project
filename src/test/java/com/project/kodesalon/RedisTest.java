//package com.project.kodesalon;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.kodesalon.domain.authentication.RefreshToken;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDateTime;
//
//@ExtendWith(SpringExtension.class)
//public class RedisTest {
//
//    @Autowired
//    RedisTemplate redisTemplate;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("")
//    void test() throws JsonProcessingException {
//        long memberId = 1L;
//        long refreshTokenId = 10L;
//
//        RefreshToken refreshToken = new RefreshToken(refreshTokenId, memberId, "testToken", LocalDateTime.now());
//        HashOperations valueOperations = redisTemplate.opsForHash();
//        String string = objectMapper.writeValueAsString(refreshToken);
//        valueOperations.put(memberId, string, string);
//        String o = (String) valueOperations.get(memberId,string);
//        RefreshToken refreshToken1 = objectMapper.readValue(o, RefreshToken.class);
//        System.out.println(refreshToken1.getToken());
//        System.out.println(refreshToken1.getExpiryDate());
//        System.out.println(refreshToken1.getMemberId());
//        System.out.println(refreshToken1.getId());
//    }
//}
