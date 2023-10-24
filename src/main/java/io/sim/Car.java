package io.sim;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoColor;

/**
 * Classe que representa um carro, que é um tipo de veículo.
 * Possui um tanque de combustível com capacidade fixa e um motorista.
 * Pode ser conduzido por uma certa distância, consumindo combustível.
 * Se o combustível acabar, precisa ser abastecido em um posto de combustível.
 */
public class Car extends Vehicle {
    // Capacidade fixa de combustível em litros
    private final double FUEL_CAPACITY = 10.0;
    // Tanque de combustível em litros
    private double fuelTank;
    // Motorista do carro
    private Driver driver;
    // Posto de combustível onde o carro pode abastecer
    private FuelStation fuelStation;

    /**
     * Construtor da classe Car.
     * Inicializa o tanque com a capacidade máxima do veículo.
     * @param driver o motorista do carro
     * @param fuelStation o posto de combustível onde o carro pode abastecer
     */
    public Car(Driver driver, FuelStation fuelStation) {
        this.fuelTank = FUEL_CAPACITY;
        this.driver = driver;
        this.fuelStation = fuelStation;
    }

    /**
     * Método que simula a condução do carro por uma certa distância.
     * Deduz o combustível com base na distância percorrida.
     * Se o combustível acabar, o carro precisa abastecer no posto de combustível.
     * @param distance a distância a ser percorrida em km
     */
    public void drive(double distance) {
        double fuelConsumptionRate = 0.1; // Taxa de consumo em litros por km (exemplo)
        double fuelConsumed = distance * fuelConsumptionRate;

        synchronized (this) {
            if (fuelTank >= fuelConsumed) {
                fuelTank -= fuelConsumed;
            } else {
                fuelConsumed = fuelTank;
                fuelTank = 0;
                fuelStation.refuelCar(this, fuelConsumed);
            }
        }
    }

    /**
     * Método que adiciona combustível ao tanque do carro.
     * Garante que o tanque não ultrapasse a capacidade máxima do veículo.
     * @param liters a quantidade de litros de combustível a ser adicionada
     */
    public synchronized void refuel(double liters) {
        fuelTank += liters;
        if (fuelTank > FUEL_CAPACITY) {
            fuelTank = FUEL_CAPACITY;
        }
    }

    /**
     * Getter para o tanque de combustível do carro.
     * @return o tanque de combustível em litros
     */
    public double getFuelTank() {
        return fuelTank;
    }

    /**
     * Getter para o motorista do carro.
     * @return o motorista do carro
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Setter para o posto de combustível do carro.
     * @param fuelStation o novo posto de combustível do carro
     */
    public void setFuelStation(FuelStation fuelStation) {
        this.fuelStation = fuelStation;
    }
}

