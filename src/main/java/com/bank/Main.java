package com.bank;

import com.bank.model.*;
import com.bank.service.BankService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BankService bankService = new BankService();
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private static final String FILE_NAME = "bank_data.json";

    public static void main(String[] args) {
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createAccount();
                case "2" -> createCategory();
                case "3" -> createOperation();
                case "4" -> calculateBalanceDifference();
                case "5" -> groupOperationsByCategory();
                case "6" -> listAccounts();
                case "7" -> listOperations();
                case "8" -> exportData();
                case "9" -> importData();
                case "0" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный ввод, попробуйте снова.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Банк Меню ===");
        System.out.println("1. Создать счет");
        System.out.println("2. Создать категорию");
        System.out.println("3. Добавить операцию");
        System.out.println("4. Подсчитать разницу доходов и расходов за период");
        System.out.println("5. Группировать операции по категориям");
        System.out.println("6. Показать счета");
        System.out.println("7. Показать операции");
        System.out.println("8. Экспорт данных");
        System.out.println("9. Импорт данных");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private static void exportData() {
        try {
            objectMapper.writeValue(new File(FILE_NAME), bankService);
            System.out.println("✅ Данные успешно экспортированы в " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("❌ Ошибка экспорта: " + e.getMessage());
        }
    }

    private static void importData() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                System.out.println("❌ Файл не найден. Экспортируйте данные перед импортом.");
                return;
            }

            BankService importedData = objectMapper.readValue(file, BankService.class);
            bankService.loadData(importedData);
            System.out.println("✅ Данные успешно импортированы.");
        } catch (IOException e) {
            System.out.println("❌ Ошибка импорта: " + e.getMessage());
        }
    }

    private static void createAccount() {
        System.out.print("Введите название счета: ");
        String name = scanner.nextLine();
        System.out.print("Введите начальный баланс: ");
        BigDecimal balance = new BigDecimal(scanner.nextLine());

        BankAccount account = bankService.createAccount(name, balance);
        System.out.println("✅ Счет создан: " + account.getId());
    }

    private static void createCategory() {
        System.out.print("Введите название категории: ");
        String name = scanner.nextLine();
        System.out.print("Тип категории (INCOME/EXPENSE): ");
        CategoryType type = CategoryType.valueOf(scanner.nextLine().toUpperCase());

        Category category = bankService.createCategory(type, name);
        System.out.println("✅ Категория создана: " + category.getId());
    }

    private static void createOperation() {
        System.out.print("ID счета: ");
        UUID accountId = UUID.fromString(scanner.nextLine());
        System.out.print("ID категории: ");
        UUID categoryId = UUID.fromString(scanner.nextLine());
        System.out.print("Сумма: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());
        System.out.print("Дата (гггг-мм-дд): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Описание: ");
        String description = scanner.nextLine();
        System.out.print("Тип операции (INCOME/EXPENSE): ");
        OperationType type = OperationType.valueOf(scanner.nextLine().toUpperCase());

        try {
            Operation operation = bankService.createOperation(type, accountId, amount, date, description, categoryId);
            System.out.println("✅ Операция добавлена: " + operation.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void calculateBalanceDifference() {
        System.out.print("Начальная дата (гггг-мм-дд): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Конечная дата (гггг-мм-дд): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        BigDecimal result = bankService.calculateBalanceDifference(startDate, endDate);
        System.out.println("📊 Разница доходов и расходов за период: " + result);
    }

    private static void groupOperationsByCategory() {
        System.out.print("Начальная дата (гггг-мм-дд): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Конечная дата (гггг-мм-дд): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        Map<String, BigDecimal> grouped = bankService.groupOperationsByCategory(startDate, endDate);
        System.out.println("📌 Группировка операций по категориям:");
        grouped.forEach((category, total) -> System.out.println(category + ": " + total));
    }

    private static void listAccounts() {
        System.out.println("📋 Список счетов:");
        for (BankAccount account : bankService.getAllAccounts()) {
            System.out.println(account.getId() + " | " + account.getName() + " | Баланс: " + account.getBalance());
        }
    }

    private static void listOperations() {
        System.out.println("📋 Список операций:");
        for (Operation operation : bankService.getAllOperations()) {
            System.out.println(operation.getId() + " | " + operation.getType() + " | " +
                    operation.getAmount() + " | " + operation.getDescription() + " | " +
                    operation.getDate() + " | Категория: " + operation.getCategory().getName());
        }
    }
}
