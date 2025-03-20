package com.bank.io;

import com.bank.model.BankAccount;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class YamlSerializerTest {
    private final YamlSerializer serializer = new YamlSerializer();

    @Test
    void testSerializeAndDeserialize() {
        List<BankAccount> accounts = Arrays.asList(
                new BankAccount("Alice", new BigDecimal("1000.50")),
                new BankAccount("Bob", new BigDecimal("2500.75"))
        );

        serializer.serialize("test_accounts.yaml", accounts);
    }
}
