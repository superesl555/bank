package com.bank;

import com.bank.io.CsvSerializer;
import com.bank.io.JsonSerializer;
import com.bank.io.YamlSerializer;
import com.bank.model.BankAccount;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // Создаем тестовые данные
        BankAccount account1 = new BankAccount("Основной счет", new BigDecimal("10000"));
        BankAccount account2 = new BankAccount("Сберегательный счет", new BigDecimal("50000"));
        List<BankAccount> accounts = List.of(account1, account2);

        // Экспорт данных
        new CsvSerializer().serialize("accounts.csv", accounts);
        new JsonSerializer().serialize("accounts.json", accounts);
        new YamlSerializer().serialize("accounts.yaml", accounts);

        // Импорт данных
        List<BankAccount> importedAccounts = new JsonSerializer().deserialize("accounts.json", BankAccount.class);
        importedAccounts.forEach(acc -> System.out.println("Импортирован счет: " + acc.getName()));
    }
}
