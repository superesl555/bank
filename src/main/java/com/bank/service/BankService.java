package com.bank.service;

import com.bank.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.*;

public class BankService {
    private final Map<UUID, BankAccount> accounts = new HashMap<>();
    private final Map<UUID, Category> categories = new HashMap<>();
    private final Map<UUID, Operation> operations = new HashMap<>();


    public BankAccount createAccount(String name, BigDecimal initialBalance) {
        BankAccount account = new BankAccount(name, initialBalance);
        accounts.put(account.getId(), account);
        return account;
    }

    public BankAccount getAccount(UUID id) {
        return accounts.get(id);
    }

    public boolean deleteAccount(UUID id) {
        return accounts.remove(id) != null;
    }

    public void updateAccountName(UUID id, String newName) {
        BankAccount account = accounts.get(id);
        if (account != null) {
            account.setName(newName);
        }
    }

    public void updateAccountBalance(UUID id, BigDecimal newBalance) {
        BankAccount account = accounts.get(id);
        if (account != null) {
            account.setBalance(newBalance);
        }
    }

    public Collection<BankAccount> getAllAccounts() {
        return accounts.values();
    }


    public Category createCategory(CategoryType type, String name) {
        Category category = new Category(type, name);
        categories.put(category.getId(), category);
        return category;
    }

    public Category getCategory(UUID id) {
        return categories.get(id);
    }

    public void updateCategoryName(UUID id, String newName) {
        Category category = categories.get(id);
        if (category != null) {
            category.setName(newName);
        }
    }

    public boolean deleteCategory(UUID id) {
        return categories.remove(id) != null;
    }

    public Collection<Category> getAllCategories() {
        return categories.values();
    }


    public Operation createOperation(OperationType type, UUID accountId, BigDecimal amount, LocalDate date, String description, UUID categoryId) {
        BankAccount account = accounts.get(accountId);
        Category category = categories.get(categoryId);
        if (account == null || category == null) {
            throw new IllegalArgumentException("Неверный счет или категория!");
        }

        Operation operation = new Operation(type, account, amount, date, description, category);
        operations.put(operation.getId(), operation);
        return operation;
    }

    public Operation getOperation(UUID id) {
        return operations.get(id);
    }

    public void updateOperation(UUID id, OperationType newType, UUID newAccountId,
                                BigDecimal newAmount, LocalDate newDate, String newDescription,
                                UUID newCategoryId) {
        Operation oldOperation = operations.remove(id); // Удаляем старую операцию
        if (oldOperation == null) return;

        BankAccount newAccount = accounts.get(newAccountId);
        Category newCategory = categories.get(newCategoryId);

        if (newAccount != null && newCategory != null) {
            Operation newOperation = new Operation(newType, newAccount, newAmount, newDate, newDescription, newCategory);
            operations.put(newOperation.getId(), newOperation);
        }
    }

    public boolean deleteOperation(UUID id) {
        return operations.remove(id) != null;
    }

    public Collection<Operation> getAllOperations() {
        return operations.values();
    }

    public BigDecimal calculateBalanceDifference(LocalDate startDate, LocalDate endDate) {
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;

        for (Operation op : operations.values()) {
            if (!op.getDate().isBefore(startDate) && !op.getDate().isAfter(endDate)) {
                if (op.getType() == OperationType.INCOME) {
                    income = income.add(op.getAmount());
                } else {
                    expense = expense.add(op.getAmount());
                }
            }
        }
        return income.subtract(expense);
    }

    public Map<String, BigDecimal> groupOperationsByCategory(LocalDate startDate, LocalDate endDate) {
        return operations.values().stream()
                .filter(op -> !op.getDate().isBefore(startDate) && !op.getDate().isAfter(endDate))
                .collect(Collectors.groupingBy(
                        op -> op.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Operation::getAmount, BigDecimal::add)
                ));
    }
}
