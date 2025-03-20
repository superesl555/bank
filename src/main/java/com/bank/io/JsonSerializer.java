package com.bank.io;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class JsonSerializer implements DataSerializer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> void serialize(String filePath, List<T> data) {
        try {
            objectMapper.writeValue(Paths.get(filePath).toFile(), data);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении JSON", e);
        }
    }

    @Override
    public <T> List<T> deserialize(String filePath, Class<T> clazz) {
        try {
            return objectMapper.readValue(
                    Paths.get(filePath).toFile(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке JSON", e);
        }
    }
}
