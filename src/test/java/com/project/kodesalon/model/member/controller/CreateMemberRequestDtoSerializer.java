package com.project.kodesalon.model.member.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.project.kodesalon.model.member.dto.CreateMemberRequestDto;

import java.io.IOException;

public class CreateMemberRequestDtoSerializer extends StdSerializer<CreateMemberRequestDto> {

    public CreateMemberRequestDtoSerializer(Class<CreateMemberRequestDto> t) {
        super(t);
    }

    @Override
    public void serialize(CreateMemberRequestDto value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("alias", value.getAlias().value());
        gen.writeStringField("password", value.getPassword().value());
        gen.writeStringField("name", value.getName().value());
        gen.writeStringField("email", value.getEmail().value());
        gen.writeStringField("phone", value.getPhone().value());
        gen.writeEndObject();
    }
}
