package com.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Operation {
    private final UUID id;
    private OperationType type;
    private BankAccount bankAccount;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private Category category;

    public Operation(OperationType type, BankAccount bankAccount, BigDecimal amount, LocalDate date, String description, Category category) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.bankAccount = bankAccount;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;

        applyOperation();
    }

    private void applyOperation() {
        if (type == OperationType.EXPENSE) {
            bankAccount.updateBalance(amount.negate());
        } else {
            bankAccount.updateBalance(amount);
        }
    }

    public UUID getId() {
        return id;
    }

    public OperationType getType() {
        return type;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

