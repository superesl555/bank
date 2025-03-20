package com.bank.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

class OperationTest {

    private BankAccount bankAccount;
    private Category category;

    @BeforeEach
    void setUp() {
        bankAccount = new BankAccount("Savings", BigDecimal.valueOf(1000));
        category = new Category(CategoryType.EXPENSE, "Food");
    }

    @Test
    void testOperationCreation() {
        Operation operation = new Operation(OperationType.EXPENSE, bankAccount,
                BigDecimal.valueOf(50), LocalDate.of(2024, 3, 15), "Groceries", category);

        assertThat(operation.getId()).isNotNull();
        assertThat(operation.getType()).isEqualTo(OperationType.EXPENSE);
        assertThat(operation.getBankAccount()).isEqualTo(bankAccount);
        assertThat(operation.getAmount()).isEqualTo(BigDecimal.valueOf(50));
        assertThat(operation.getDate()).isEqualTo(LocalDate.of(2024, 3, 15));
        assertThat(operation.getDescription()).isEqualTo("Groceries");
        assertThat(operation.getCategory()).isEqualTo(category);
    }

    @Test
    void testApplyExpenseOperation() {
        BigDecimal initialBalance = bankAccount.getBalance();
        new Operation(OperationType.EXPENSE, bankAccount,
                BigDecimal.valueOf(100), LocalDate.now(), "Dinner", category);

        assertThat(bankAccount.getBalance()).isEqualTo(initialBalance.subtract(BigDecimal.valueOf(100)));
    }

    @Test
    void testApplyIncomeOperation() {
        BigDecimal initialBalance = bankAccount.getBalance();
        new Operation(OperationType.INCOME, bankAccount,
                BigDecimal.valueOf(200), LocalDate.now(), "Salary", category);

        assertThat(bankAccount.getBalance()).isEqualTo(initialBalance.add(BigDecimal.valueOf(200)));
    }

    @Test
    void testSetType() {
        Operation operation = new Operation(OperationType.EXPENSE, bankAccount,
                BigDecimal.valueOf(50), LocalDate.now(), "Shopping", category);

        operation.setType(OperationType.INCOME);
        assertThat(operation.getType()).isEqualTo(OperationType.INCOME);
    }

    @Test
    void testSetAmount() {
        Operation operation = new Operation(OperationType.EXPENSE, bankAccount,
                BigDecimal.valueOf(50), LocalDate.now(), "Coffee", category);

        operation.setAmount(BigDecimal.valueOf(75));
        assertThat(operation.getAmount()).isEqualTo(BigDecimal.valueOf(75));
    }

    @Test
    void testSetDate() {
        Operation operation = new Operation(OperationType.EXPENSE, bankAccount,
                BigDecimal.valueOf(30), LocalDate.now(), "Breakfast", category);

        LocalDate newDate = LocalDate.of(2024, 4, 10);
        operation.setDate(newDate);
        assertThat(operation.getDate()).isEqualTo(newDate);
    }

    @Test
    void testSetDescription() {
        Operation operation = new Operation(OperationType.EXPENSE, bankAccount,
                BigDecimal.valueOf(20), LocalDate.now(), "Lunch", category);

        operation.setDescription("Dinner");
        assertThat(operation.getDescription()).isEqualTo("Dinner");
    }

    @Test
    void testSetCategory() {
        Operation operation = new Operation(OperationType.EXPENSE, bankAccount,
                BigDecimal.valueOf(40), LocalDate.now(), "Snacks", category);

        Category newCategory = new Category(CategoryType.INCOME, "Salary");
        operation.setCategory(newCategory);
        assertThat(operation.getCategory()).isEqualTo(newCategory);
    }

    @Test
    void testSetBankAccount() {
        BankAccount newAccount = new BankAccount("Checking", BigDecimal.valueOf(500));
        Operation operation = new Operation(OperationType.EXPENSE, bankAccount,
                BigDecimal.valueOf(100), LocalDate.now(), "Rent", category);

        operation.setBankAccount(newAccount);
        assertThat(operation.getBankAccount()).isEqualTo(newAccount);
    }
}
