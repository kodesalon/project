package com.project.kodesalon.model.member.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.project.kodesalon.model.member.dto.LoginRequestDto;

import java.io.IOException;

public class LoginRequestDtoSerializer extends StdSerializer<LoginRequestDto> {
    public LoginRequestDtoSerializer() {
        this(null);
    }

    public LoginRequestDtoSerializer(Class<LoginRequestDto> t) {
        super(t);
    }

    @Override
    public void serialize(LoginRequestDto value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("alias", value.getAlias().value());
        gen.writeStringField("password", value.getPassword().value());
        gen.writeEndObject();
    }
}
