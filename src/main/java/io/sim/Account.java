package io.sim;

public class Account {
    private String accountNumber;
    private double balance;

    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Depósito de $" + amount + " realizado na conta " + accountNumber);
        } else {
            System.out.println("Valor inválido para depósito");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println("Retirada de $" + amount + " realizada na conta " + accountNumber);
        } else {
            System.out.println("Saldo insuficiente ou valor inválido para retirada");
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String toString() {
        return "Conta " + accountNumber + ": Saldo = $" + balance;
    }
}
