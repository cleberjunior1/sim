package io.sim;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AlphaBank extends Thread {
    private double companyBalance;
    private double fuelStationBalance;
    private Map<Driver, Double> driverBalances;
    private Lock lock;

    public AlphaBank() {
        this.companyBalance = 0.0;
        this.fuelStationBalance = 0.0;
        this.driverBalances = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    public synchronized void depositToCompany(double amount) {
        lock.lock();
        try {
            companyBalance += amount;
        } finally {
            lock.unlock();
        }
    }

    public synchronized void depositToFuelStation(double amount) {
        lock.lock();
        try {
            fuelStationBalance += amount;
        } finally {
            lock.unlock();
        }
    }

    public synchronized double getCompanyBalance() {
        lock.lock();
        try {
            return companyBalance;
        } finally {
            lock.unlock();
        }
    }

    public synchronized double getFuelStationBalance() {
        lock.lock();
        try {
            return fuelStationBalance;
        } finally {
            lock.unlock();
        }
    }

    public synchronized boolean checkBalance(Driver driver, double amount) {
        lock.lock();
        try {
            if (driverBalances.containsKey(driver)) {
                double balance = driverBalances.get(driver);
                return balance >= amount;
            }
            return false; // O motorista não possui uma conta no banco ou não tem saldo suficiente
        } finally {
            lock.unlock();
        }
    }

    public synchronized void makePayment(Driver driver, double amount) {
        lock.lock();
        try {
            if (driverBalances.containsKey(driver)) {
                double balance = driverBalances.get(driver);
                if (balance >= amount) {
                    balance -= amount;
                    driverBalances.put(driver, balance);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public synchronized void createAccount(Driver driver, double initialBalance) {
        lock.lock();
        try {
            driverBalances.put(driver, initialBalance);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        // Lógica da thread do banco, se necessário
    }
}
