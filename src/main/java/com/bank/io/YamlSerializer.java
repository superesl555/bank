package com.bank.io;

import com.bank.model.BankAccount;
import com.bank.model.Category;
import com.bank.model.Operation;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class YamlSerializer implements DataSerializer {

    private final Yaml yaml;

    public YamlSerializer() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // Читаемый YAML
        yaml = new Yaml(options);
    }

    @Override
    public <T> void serialize(String filePath, List<T> data) {
        try (Writer writer = Files.newBufferedWriter(Path.of(filePath))) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении YAML", e);
        }
    }

    @Override
    public <T> List<T> deserialize(String filePath, Class<T> clazz) {
        try (Reader reader = Files.newBufferedReader(Path.of(filePath))) {
            return yaml.load(reader);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке YAML", e);
        }
    }
}
