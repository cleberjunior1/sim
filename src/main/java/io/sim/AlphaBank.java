package io.sim;

import java.util.Map;
import java.util.HashMap;

public class AlphaBank extends Thread {
    private double companyBalance;
    private double fuelStationBalance;
    private Map<Driver, Double> accountBalances;

    public AlphaBank() {
        // Inicialize os saldos iniciais para a empresa e a estação de abastecimento
        companyBalance = 0.0;
        fuelStationBalance = 0.0;
        this.accountBalances = new HashMap<>();
    }

    // Métodos para realizar transações financeiras

    public synchronized void depositToCompany(double amount) {
        companyBalance += amount;
    }

    public synchronized void depositToFuelStation(double amount) {
        fuelStationBalance += amount;
    }

    public synchronized double getCompanyBalance() {
        return companyBalance;
    }

    public synchronized double getFuelStationBalance() {
        return fuelStationBalance;
    }

    public synchronized boolean checkBalance(Driver driver, double amount) {
        if (accountBalances.containsKey(driver)) {
            double balance = accountBalances.get(driver);
            return balance >= amount;
        }
        return false; // O motorista não possui uma conta no banco ou não tem saldo suficiente
    }

    public synchronized void makePayment(Driver driver, double amount) {
        // Implemente a lógica para fazer o pagamento
    }

    @Override
    public void run() {
        // Lógica da thread do banco, se necessário
    }
}
