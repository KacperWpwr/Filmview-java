package com.example.filmview.Filmview.Mapper;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.apache.bcel.classfile.JavaClass;

import java.io.IOException;

public class JSONMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T mapToObject(String content, Class<T> classType) throws IOException {
        return objectMapper.readValue(content,classType);
    }


}
