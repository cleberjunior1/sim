package io.sim;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoColor;

public class Car extends Vehicle {
    private final double FUEL_CAPACITY = 10.0; // Capacidade fixa de combustível em litros
    private double fuelTank; // Tanque de combustível em litros
    private Driver driver;
    private FuelStation fuelStation;

    public Car(Driver driver, FuelStation fuelStation) {
        this.fuelTank = FUEL_CAPACITY; // Inicializa o tanque com a capacidade máxima do veículo
        this.driver = driver;
        this.fuelStation = fuelStation;
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
                fuelConsumed = fuelTank; // Abastece apenas o que resta no tanque
                fuelTank = 0;
                fuelStation.refuelCar(this, fuelConsumed);
            }
        }
    }

    public synchronized void refuel(double liters) {
        // Método para a FuelStation adicionar combustível ao carro
        fuelTank += liters;
        // Garante que o tanque não ultrapasse a capacidade máxima do veículo
        if (fuelTank > FUEL_CAPACITY) {
            fuelTank = FUEL_CAPACITY;
        }
    }

    public double getFuelTank() {
        return fuelTank;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setFuelStation(FuelStation fuelStation) {
        this.fuelStation = fuelStation;
    }

    // Outros métodos da classe Car
}

