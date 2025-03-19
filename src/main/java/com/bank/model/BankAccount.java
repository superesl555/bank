package com.bank.model;

import java.math.BigDecimal;
import java.util.UUID;

public class BankAccount {
    private final UUID id;
    private String name;
    private BigDecimal balance;

    public BankAccount(String name, BigDecimal initialBalance) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.balance = initialBalance;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void updateBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
}
