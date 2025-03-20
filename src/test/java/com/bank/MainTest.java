package com.bank;

import org.junit.jupiter.api.*;
import java.io.*;
import static org.assertj.core.api.Assertions.*;

class MainTest {

    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setUpStreams() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void restoreStreams() {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    void testMainMenuDisplaysCorrectly() {
        provideInput("exit\n"); // Симулируем ввод "exit"

        Main.main(new String[]{}); // Запускаем main()

        String output = testOut.toString();
        assertThat(output).contains("Добро пожаловать в банковское приложение");
        assertThat(output).contains("Введите команду:");
    }

    @Test
    void testInvalidCommandHandling() {
        provideInput("unknown_command\nexit\n");

        Main.main(new String[]{});

        String output = testOut.toString();
        assertThat(output).contains("Неизвестная команда");
    }

    @Test
    void testCreateAccount() {
        provideInput("create_account Сбережения 1000\nexit\n");

        Main.main(new String[]{});

        String output = testOut.toString();
        assertThat(output).contains("Счет 'Сбережения' создан с балансом 1000.00");
    }

    @Test
    void testDepositMoney() {
        provideInput("create_account Основной 500\n" +
                "deposit Основной 200\n" +
                "exit\n");

        Main.main(new String[]{});

        String output = testOut.toString();
        assertThat(output).contains("Счет 'Основной' создан с балансом 500.00");
        assertThat(output).contains("На счет 'Основной' внесено 200.00");
    }

    @Test
    void testWithdrawMoney() {
        provideInput("create_account Кошелек 1000\n" +
                "withdraw Кошелек 300\n" +
                "exit\n");

        Main.main(new String[]{});

        String output = testOut.toString();
        assertThat(output).contains("Счет 'Кошелек' создан с балансом 1000.00");
        assertThat(output).contains("Со счета 'Кошелек' снято 300.00");
    }

    @Test
    void testCheckBalance() {
        provideInput("create_account Депозит 2000\n" +
                "balance Депозит\n" +
                "exit\n");

        Main.main(new String[]{});

        String output = testOut.toString();
        assertThat(output).contains("Баланс счета 'Депозит': 2000.00");
    }

    @Test
    void testExitCommand() {
        provideInput("exit\n");

        Main.main(new String[]{});

        String output = testOut.toString();
        assertThat(output).contains("Выход из приложения");
    }
}

