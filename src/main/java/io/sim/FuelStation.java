package io.sim;

import java.util.concurrent.Semaphore;

public class FuelStation extends Thread {
    private AlphaBank alphaBank;
    private double fuelPricePerLiter;
    private Semaphore fuelPumps;
    private double totalPayments;
    private double fuelStock; // Estoque de combustível em litros

    public FuelStation(AlphaBank alphaBank, int numFuelPumps, double fuelPricePerLiter, double initialFuelStock) {
        this.alphaBank = alphaBank;
        this.fuelPricePerLiter = fuelPricePerLiter;
        this.fuelPumps = new Semaphore(numFuelPumps, true);
        this.totalPayments = 0.0;
        this.fuelStock = initialFuelStock;
    }

    @Override
    public void run() {
        // Lógica da FuelStation (por exemplo, aguardar carros para abastecer)
    }

    public void refuelCar(Car car, double fuelConsumed) {
        try {
            fuelPumps.acquire(); // Tenta adquirir uma bomba de combustível
            synchronized (car) {
                double remainingFuel = car.getFuelTank();
                double litersToAdd = 10.00 - remainingFuel;
                double cost = litersToAdd * fuelPricePerLiter;
    
                // Verifica se o driver tem saldo suficiente no AlphaBank para pagar o abastecimento
                if (alphaBank.checkBalance(car.getDriver(), cost)) {
                    // Verifique se há combustível suficiente na estação
                    if (hasEnoughFuel(litersToAdd)) {
                        // Abastece o carro e deduz o valor do saldo do driver
                        car.refuel(litersToAdd);
                        alphaBank.makePayment(car.getDriver(), cost);
    
                        // Adiciona o pagamento à FuelStation
                        addPayment(cost);
    
                        // Deduz o combustível do estoque da estação
                        consumeFuel(litersToAdd);
    
                        System.out.println("Carro abastecido com sucesso. Motorista: " + car.getDriver().getName());
                    } else {
                        System.out.println("Estoque de combustível insuficiente para abastecer. Motorista: " + car.getDriver().getName());
                    }
                } else {
                    System.out.println("Saldo insuficiente para abastecer. Motorista: " + car.getDriver().getName());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            fuelPumps.release(); // Libera a bomba de combustível após o abastecimento
        }
    }
    

    public synchronized void addPayment(double amount) {
        totalPayments += amount;
    }

    public double getTotalPayments() {
        return totalPayments;
    }

    public double getFuelStock() {
        return fuelStock;
    }

    public synchronized boolean hasEnoughFuel(double liters) {
        return fuelStock >= liters;
    }

    public synchronized void consumeFuel(double liters) {
        if (hasEnoughFuel(liters)) {
            fuelStock -= liters;
        }
    }

    // Outros métodos da classe
}
