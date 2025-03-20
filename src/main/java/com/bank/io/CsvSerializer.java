package com.bank.io;

import com.bank.model.BankAccount;
import com.bank.model.Category;
import com.bank.model.Operation;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvSerializer implements DataSerializer {

    @Override
    public <T> void serialize(String filePath, List<T> data) {
        if (data.isEmpty()) return; // Нет смысла писать пустой файл

        try (Writer writer = Files.newBufferedWriter(Path.of(filePath))) {
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer).build();
            beanToCsv.write(data);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении CSV", e);
        }
    }

    @Override
    public <T> List<T> deserialize(String filePath, Class<T> clazz) {
        try (Reader reader = Files.newBufferedReader(Path.of(filePath))) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке CSV", e);
        }
    }
}
