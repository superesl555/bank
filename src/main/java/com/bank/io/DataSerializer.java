package com.bank.io;

import java.util.List;

public interface DataSerializer {
    <T> void serialize(String filePath, List<T> data);
    <T> List<T> deserialize(String filePath, Class<T> clazz);
}
