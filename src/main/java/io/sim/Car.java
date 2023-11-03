package io.sim;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoColor;
import it.polito.appeal.traci.SumoTraciConnection;

public class Car extends Vehicle implements Runnable {
    // Deve ser cliente de Company
    // Sumo deve retornar a quantidade de litros gastos e o valor de Fuel Tank deve
    // ser alterado
    private double FuelTank;
    private boolean isAlive = false;
    private boolean abastecer;

    private String idCarro;
    private Auto auto;
    private SumoTraciConnection sumo;

    public Car(String idDriver, SumoTraciConnection sumo) {
        this.isAlive = true;
        this.FuelTank = 10;
        this.abastecer = false;
        this.idCarro = "Carro_" + idDriver;
        this.sumo = sumo;
        int fuelType = 2;
        int fuelPreferential = 2;
        double fuelPrice = 5.87;
        int personCapacity = 1;
        int personNumber = 1;
        SumoColor green = new SumoColor(0, 255, 0, 126);
        try {
            this.auto = new Auto(true, idCarro, green, idDriver, this.sumo, 500, fuelType, fuelPreferential, fuelPrice,
                    personCapacity, personNumber);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        run();
    }

    public void run() {
        // Processos iniciais...
        while (isAlive) {
            try {
                if (FuelTank <= 3) { // Verificando a quantidade de combustível
                    abastecer = true; // Precisa abastecer
                }
                // System.out.println("Thread botpayment");
                Thread.sleep(1000);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    public void atualizaCombustivel(double km, double consumo) {
        double litrosConsumidos = km * consumo;
        this.FuelTank -= litrosConsumidos;
    }

    public void infoTanqueAbastecido(double gasolina) {
        this.FuelTank = gasolina;
        this.abastecer = false;
    }

    public double getFuelTank() {
        return FuelTank;
    }

    public boolean getAbastecer() {
        return abastecer; // Sinal de que o carro foi abastecido
    }

    public void adicinaGasolina(double gasolina) {
        this.FuelTank += gasolina;
    }

    public void paraCarro() {

    }

    public void liberaCarro() {

    }

    public Auto getAuto() {
        return this.auto;
    }
}