package com.bank.service;

import com.bank.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

class BankServiceTest {
    private BankService bankService;
    private BankAccount testAccount;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        bankService = new BankService();
        testAccount = bankService.createAccount("Test Account", new BigDecimal("1000.00"));
        testCategory = bankService.createCategory(CategoryType.EXPENSE, "Groceries");
    }

    @Test
    void shouldCreateAndRetrieveAccount() {
        UUID accountId = testAccount.getId();
        BankAccount retrieved = bankService.getAccount(accountId);
        assertThat(retrieved).isNotNull()
                .extracting(BankAccount::getName, BankAccount::getBalance)
                .containsExactly("Test Account", new BigDecimal("1000.00"));
    }

    @Test
    void shouldUpdateAccountNameAndBalance() {
        UUID accountId = testAccount.getId();
        bankService.updateAccountName(accountId, "Updated Name");
        bankService.updateAccountBalance(accountId, new BigDecimal("1500.00"));

        BankAccount updated = bankService.getAccount(accountId);
        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getBalance()).isEqualByComparingTo(new BigDecimal("1500.00"));
    }

    @Test
    void shouldDeleteAccount() {
        UUID accountId = testAccount.getId();
        boolean deleted = bankService.deleteAccount(accountId);
        assertThat(deleted).isTrue();
        assertThat(bankService.getAccount(accountId)).isNull();
    }

    @Test
    void shouldCreateAndRetrieveCategory() {
        UUID categoryId = testCategory.getId();
        Category retrieved = bankService.getCategory(categoryId);
        assertThat(retrieved).isNotNull()
                .extracting(Category::getName, Category::getType)
                .containsExactly("Groceries", CategoryType.EXPENSE);
    }

    @Test
    void shouldUpdateCategoryName() {
        UUID categoryId = testCategory.getId();
        bankService.updateCategoryName(categoryId, "Updated Category");

        Category updated = bankService.getCategory(categoryId);
        assertThat(updated.getName()).isEqualTo("Updated Category");
    }

    @Test
    void shouldDeleteCategory() {
        UUID categoryId = testCategory.getId();
        boolean deleted = bankService.deleteCategory(categoryId);
        assertThat(deleted).isTrue();
        assertThat(bankService.getCategory(categoryId)).isNull();
    }

    @Test
    void shouldCreateAndRetrieveOperation() {
        UUID accountId = testAccount.getId();
        UUID categoryId = testCategory.getId();
        LocalDate date = LocalDate.of(2024, 1, 15);

        Operation operation = bankService.createOperation(
                OperationType.EXPENSE, accountId, new BigDecimal("200.00"), date, "Test purchase", categoryId);

        Operation retrieved = bankService.getOperation(operation.getId());
        assertThat(retrieved).isNotNull()
                .extracting(Operation::getType, Operation::getAmount, Operation::getDescription)
                .containsExactly(OperationType.EXPENSE, new BigDecimal("200.00"), "Test purchase");
    }

    @Test
    void shouldCalculateBalanceDifferenceForPeriod() {
        UUID accountId = testAccount.getId();
        UUID categoryId = testCategory.getId();
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);

        bankService.createOperation(OperationType.INCOME, accountId, new BigDecimal("3000.00"), LocalDate.of(2024, 1, 10), "Salary", categoryId);
        bankService.createOperation(OperationType.EXPENSE, accountId, new BigDecimal("500.00"), LocalDate.of(2024, 1, 15), "Rent", categoryId);
        bankService.createOperation(OperationType.EXPENSE, accountId, new BigDecimal("300.00"), LocalDate.of(2024, 1, 20), "Food", categoryId);

        BigDecimal balanceDiff = bankService.calculateBalanceDifference(start, end);
        assertThat(balanceDiff).isEqualByComparingTo(new BigDecimal("2200.00")); // 3000 - 500 - 300
    }

    @Test
    void shouldGroupOperationsByCategory() {
        UUID accountId = testAccount.getId();
        UUID groceriesCategoryId = testCategory.getId();
        UUID rentCategoryId = bankService.createCategory(CategoryType.EXPENSE, "Rent").getId();
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);

        bankService.createOperation(OperationType.EXPENSE, accountId, new BigDecimal("100.00"), LocalDate.of(2024, 1, 5), "Groceries", groceriesCategoryId);
        bankService.createOperation(OperationType.EXPENSE, accountId, new BigDecimal("200.00"), LocalDate.of(2024, 1, 10), "More Groceries", groceriesCategoryId);
        bankService.createOperation(OperationType.EXPENSE, accountId, new BigDecimal("500.00"), LocalDate.of(2024, 1, 15), "Rent", rentCategoryId);

        Map<String, BigDecimal> grouped = bankService.groupOperationsByCategory(start, end);

        assertThat(grouped)
                .containsEntry("Groceries", new BigDecimal("300.00"))
                .containsEntry("Rent", new BigDecimal("500.00"));
    }
}
