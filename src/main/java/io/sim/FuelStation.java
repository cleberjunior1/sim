package io.sim;

import java.util.concurrent.Semaphore;

public class FuelStation extends Thread {
    private AlphaBank alphaBank;
    private double fuelPricePerLiter;
    private Semaphore fuelPumps;
    private double totalPayments;

    public FuelStation(AlphaBank alphaBank, int numFuelPumps, double fuelPricePerLiter) {
        this.alphaBank = alphaBank;
        this.fuelPricePerLiter = fuelPricePerLiter;
        this.fuelPumps = new Semaphore(numFuelPumps, true);
        this.totalPayments = 0.0;
    }

    @Override
    public void run() {
        // Lógica da FuelStation (por exemplo, aguardar carros para abastecer)
    }

    public void refuelCar(Car car) {
        try {
            fuelPumps.acquire(); // Tenta adquirir uma bomba de combustível
            synchronized (car) {
                double remainingFuel = car.getFuelTank();
                double litersToAdd = car.getFuelCapacity() - remainingFuel;
                double cost = litersToAdd * fuelPricePerLiter;

                // Verifica se o driver tem saldo suficiente no AlphaBank para pagar o abastecimento
                if (alphaBank.checkBalance(car.getDriver(), cost)) {
                    // Abastece o carro e deduz o valor do saldo do driver
                    car.refuel(litersToAdd);
                    alphaBank.makePayment(car.getDriver(), cost);

                    // Adiciona o pagamento à FuelStation
                    addPayment(cost);

                    System.out.println("Carro abastecido com sucesso. Motorista: " + car.getDriver().getName());
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

    // Outros métodos da classe FuelStation
}
