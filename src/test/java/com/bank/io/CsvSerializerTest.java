package com.bank.io;

import com.bank.model.BankAccount;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvSerializerTest {
    private final CsvSerializer serializer = new CsvSerializer();


    @Test
    void testSerializeAndDeserialize() {
        List<BankAccount> accounts = Arrays.asList(
                new BankAccount("Alice", new BigDecimal("1000.50")),
                new BankAccount("Bob", new BigDecimal("2500.75"))
        );

        serializer.serialize("test_accounts.csv", accounts);

        List<BankAccount> loadedAccounts = serializer.deserialize("test_accounts.csv", BankAccount.class);

        assertThat(loadedAccounts).hasSize(2);
        assertThat(loadedAccounts.get(0).getName()).isEqualTo("Alice");
        assertThat(loadedAccounts.get(1).getBalance()).isEqualTo(new BigDecimal("2500.75"));
    }
}
