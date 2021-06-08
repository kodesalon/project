package com.project.kodesalon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.project.kodesalon.config.serializer.LoginRequestDtoSerializer;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class JacksonConfiguration {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LoginRequestDto.class, new LoginRequestDtoSerializer());
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }
}
