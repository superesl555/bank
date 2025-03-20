package com.bank.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

class BankAccountTest {

    @Test
    void shouldCreateBankAccountWithInitialBalance() {
        String name = "John Doe";
        BigDecimal initialBalance = new BigDecimal("1000.50");

        BankAccount account = new BankAccount(name, initialBalance);

        assertThat(account.getId()).isNotNull();
        assertThat(account.getName()).isEqualTo(name);
        assertThat(account.getBalance()).isEqualByComparingTo(initialBalance);
    }

    @Test
    void shouldUpdateBalanceCorrectly() {
        BankAccount account = new BankAccount("Alice", new BigDecimal("500.00"));

        account.updateBalance(new BigDecimal("200.00"));

        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("700.00"));
    }

    @Test
    void shouldAllowBalanceToBeSetDirectly() {
        BankAccount account = new BankAccount("Bob", new BigDecimal("300.00"));

        account.setBalance(new BigDecimal("900.00"));

        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("900.00"));
    }

    @Test
    void shouldAllowNameToBeChanged() {
        BankAccount account = new BankAccount("Charlie", new BigDecimal("100.00"));

        account.setName("Charlie Updated");

        assertThat(account.getName()).isEqualTo("Charlie Updated");
    }
}
