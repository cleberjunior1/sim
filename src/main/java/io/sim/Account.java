package io.sim;

/**
 * A class representing a bank account.
 */
public class Account {
    private String accountNumber; // The account number for this account
    private double balance; // The current balance of this account

    /**
     * Constructs a new Account object with the given account number and initial balance.
     *
     * @param accountNumber the account number for this account
     * @param initialBalance the initial balance for this account
     */
    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    /**
     * Deposits the given amount into this account.
     *
     * @param amount the amount to deposit
     */
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Depósito de $" + amount + " realizado na conta " + accountNumber);
        } else {
            System.out.println("Valor inválido para depósito");
        }
    }

    /**
     * Withdraws the given amount from this account.
     *
     * @param amount the amount to withdraw
     */
    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println("Retirada de $" + amount + " realizada na conta " + accountNumber);
        } else {
            System.out.println("Saldo insuficiente ou valor inválido para retirada");
        }
    }

    /**
     * Returns the current balance of this account.
     *
     * @return the current balance of this account
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Returns the account number for this account.
     *
     * @return the account number for this account
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Returns a string representation of this account.
     *
     * @return a string representation of this account
     */
    @Override
    public String toString() {
        return "Conta " + accountNumber + ": Saldo = $" + balance;
    }
}
