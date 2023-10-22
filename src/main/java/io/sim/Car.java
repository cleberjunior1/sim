package io.sim;

import de.tudresden.sumo.cmd.Vehicle;

public class Car extends Vehicle {
    private double fuelTank; // Tanque de combustível em litros
    private Driver driver;
    private AlphaBank alphaBank;
    private FuelStation fuelStation;
    private double fuelCapacity;

    public Car(double initialFuel, Driver driver) {
        this.fuelTank = initialFuel;
        this.driver = driver;
        this.fuelCapacity = 10.00;
        this.driver = driver;
    }

    public void drive(double distance) {
        // Simulação de condução do carro
        // Deduz o combustível com base na distância percorrida
        double fuelConsumptionRate = 0.1; // Taxa de consumo em litros por km (exemplo)
        double fuelConsumed = distance * fuelConsumptionRate;
        
        synchronized (this) {
            if (fuelTank >= fuelConsumed) {
                fuelTank -= fuelConsumed;
            } else {
                // Caso o combustível não seja suficiente, o carro precisa abastecer na FuelStation
                fuelTank = 0;
                fuelStation.refuelCar(this);
            }
        }
    }

    public synchronized void refuel(double liters) {
        // Método para a FuelStation adicionar combustível ao carro
        fuelTank += liters;
    }

    public double getFuelTank() {
        return fuelTank;
    }

    public double getFuelCapacity() {
        return fuelCapacity;
    }

    public Driver getDriver(){
        return driver;
    }

    // Outros métodos da classe Car
}
