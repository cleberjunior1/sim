package io.sim;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class represents a bank that manages the financial transactions of a simulation system.
 * It keeps track of the company balance, fuel station balance, and driver balances.
 */
public class AlphaBank extends Thread {
    private double companyBalance; // The balance of the company
    private double fuelStationBalance; // The balance of the fuel station
    private Map<Driver, Double> driverBalances; // A map of driver balances
    private Lock lock; // A lock to ensure thread safety

    /**
     * Constructs a new AlphaBank object with initial balances of 0.0 and an empty driver balance map.
     */
    public AlphaBank() {
        this.companyBalance = 0.0;
        this.fuelStationBalance = 0.0;
        this.driverBalances = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    /**
     * Deposits the specified amount to the company balance.
     * @param amount The amount to deposit
     */
    public synchronized void depositToCompany(double amount) {
        lock.lock();
        try {
            companyBalance += amount;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Deposits the specified amount to the fuel station balance.
     * @param amount The amount to deposit
     */
    public synchronized void depositToFuelStation(double amount) {
        lock.lock();
        try {
            fuelStationBalance += amount;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the current company balance.
     * @return The company balance
     */
    public synchronized double getCompanyBalance() {
        lock.lock();
        try {
            return companyBalance;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the current fuel station balance.
     * @return The fuel station balance
     */
    public synchronized double getFuelStationBalance() {
        lock.lock();
        try {
            return fuelStationBalance;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Checks if the specified driver has a balance greater than or equal to the specified amount.
     * @param driver The driver to check
     * @param amount The amount to check for
     * @return True if the driver has a balance greater than or equal to the specified amount, false otherwise
     */
    public synchronized boolean checkBalance(Driver driver, double amount) {
        lock.lock();
        try {
            if (driverBalances.containsKey(driver)) {
                double balance = driverBalances.get(driver);
                return balance >= amount;
            }
            return false; // The driver does not have an account or does not have enough balance
        } finally {
            lock.unlock();
        }
    }

    /**
     * Makes a payment from the specified driver's balance.
     * @param driver The driver to make the payment from
     * @param amount The amount to pay
     */
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

    /**
     * Creates a new account for the specified driver with the specified initial balance.
     * @param driver The driver to create the account for
     * @param initialBalance The initial balance of the account
     */
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
        // This method is empty because it is not used in this class
    }
}
