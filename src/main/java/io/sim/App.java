package io.sim;

public class App {
    public static void main(String[] args) {
        
        AlphaBank alphaBank = new AlphaBank();
        double initialFuelStock = 1000.0; // Defina o estoque inicial de combustível desejado

        FuelStation fuelStation = new FuelStation(alphaBank, 2, 5.87, initialFuelStock); // 2 bombas, preço do combustível e estoque inicial

        Company company = new Company();

        EnvSimulator envSimulator = new EnvSimulator(company);

        // Inicie a FuelStation em uma thread separada
        fuelStation.start();

        // Inicie a simulação da empresa
        company.start();

        // Inicie a simulação do AlphaBank
        alphaBank.start();

        envSimulator.start();

        // Aguarde a conclusão da simulação
        try {
            company.join();
            fuelStation.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Realize qualquer limpeza ou relatórios finais, se necessário
    }
}
