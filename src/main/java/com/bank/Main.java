package com.bank;
import com.bank.model.*;
import com.bank.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        BankService bankService = new BankService();

        // Создаем счета
        BankAccount mainAccount = bankService.createAccount("Основной счет", BigDecimal.valueOf(1000));

        // Создаем категории
        Category salaryCategory = bankService.createCategory(CategoryType.INCOME, "Зарплата");
        Category cafeCategory = bankService.createCategory(CategoryType.EXPENSE, "Кафе");
        Category healthCategory = bankService.createCategory(CategoryType.EXPENSE, "Здоровье");

        // Добавляем операции
        bankService.createOperation(OperationType.INCOME, mainAccount.getId(), BigDecimal.valueOf(3000), LocalDate.of(2024, 3, 1), "Зарплата", salaryCategory.getId());
        bankService.createOperation(OperationType.EXPENSE, mainAccount.getId(), BigDecimal.valueOf(400), LocalDate.of(2024, 3, 3), "Обед в кафе", cafeCategory.getId());
        bankService.createOperation(OperationType.EXPENSE, mainAccount.getId(), BigDecimal.valueOf(600), LocalDate.of(2024, 3, 5), "Лекарства", healthCategory.getId());

        // Обновление счета
        bankService.updateAccountName(mainAccount.getId(), "Личный счет");
        bankService.updateAccountBalance(mainAccount.getId(), BigDecimal.valueOf(5000));

        // Обновление категории
        bankService.updateCategoryName(cafeCategory.getId(), "Рестораны");

        // Обновление операции (меняем сумму и описание)
        UUID someOperationId = bankService.getAllOperations().iterator().next().getId();
        bankService.updateOperation(someOperationId, OperationType.INCOME, mainAccount.getId(), BigDecimal.valueOf(3500), LocalDate.of(2024, 3, 1), "Повышенная зарплата", salaryCategory.getId());

        // Подсчет баланса за период
        BigDecimal balanceDiff = bankService.calculateBalanceDifference(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        System.out.println("Разница доходов и расходов за март 2024: " + balanceDiff);

        // Группировка доходов и расходов по категориям
        Map<String, BigDecimal> groupedOperations = bankService.groupOperationsByCategory(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 31));
        System.out.println("\nСуммы по категориям:");
        for (Map.Entry<String, BigDecimal> entry : groupedOperations.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}