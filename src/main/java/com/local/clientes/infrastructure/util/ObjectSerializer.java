package com.local.clientes.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ObjectSerializer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String serialize(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    public <T>T deserialize(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }

}
