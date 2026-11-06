package edu.course.lab02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void withdrawReducesBalance() {
        BankAccount account = new BankAccount(100);
        account.withdraw(30);
        assertEquals(70, account.getBalance());
    }

    @Test
    void withdrawMoreThanBalanceThrows() {
        BankAccount account = new BankAccount(50);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(51));
        assertEquals(50, account.getBalance());
    }

    @Test
    void negativeInitialBalanceThrows() {
        assertThrows(IllegalArgumentException.class, () -> new BankAccount(-1));
    }

    @Test
    void depositIncreasesBalance() {
        BankAccount account = new BankAccount(10);
        account.deposit(5);
        assertEquals(15, account.getBalance());
    }

    @Test
    void depositNonPositiveThrows() {
        BankAccount account = new BankAccount(10);
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-5));
    }

    @Test
    void withdrawNonPositiveThrows() {
        BankAccount account = new BankAccount(10);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-1));
    }
}