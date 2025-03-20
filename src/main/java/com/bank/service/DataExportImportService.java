package com.bank.service;

import com.bank.io.DataSerializer;
import java.util.List;

public class DataExportImportService {
    private final DataSerializer serializer;

    public DataExportImportService(DataSerializer serializer) {
        this.serializer = serializer;
    }

    public <T> void exportData(String filePath, List<T> data) {
        serializer.serialize(filePath, data);
    }

    public <T> List<T> importData(String filePath, Class<T> type) {
        return serializer.deserialize(filePath, type);
    }
}
