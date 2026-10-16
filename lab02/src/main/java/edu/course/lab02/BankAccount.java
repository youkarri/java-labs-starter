package edu.course.lab02;

/**
 * Учебный банковский счет. Баланс никогда не бывает отрицательным.
 */
public final class BankAccount {

    private int balance;

    public BankAccount(int initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("initial balance must not be negative");
        }
        this.balance = initialBalance;
    }

    public void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        balance += amount;
    }

    public void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("amount exceeds balance");
        }
        balance -= amount;
    }

    public int getBalance() {
        return balance;
    }
}