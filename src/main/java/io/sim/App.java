package io.sim;

public class App {
    public static void main(String[] args) {
        EnvSimulator envSimulator = new EnvSimulator();
        AlphaBank alphaBank = new AlphaBank();
        FuelStation fuelStation = new FuelStation(alphaBank, 2, 5.87); // 2 bombas, preço do combustível
        Company company = new Company(envSimulator);

        // Inicie o ambiente de simulação (SUMO)
        envSimulator.start();

        // Inicie a FuelStation em uma thread separada
        fuelStation.start();

        // Inicie a simulação da empresa
        company.start();

        // Inicie a simulação do AlphaBank
        alphaBank.start();

        // Aguarde a conclusão da simulação
        try {
            company.join();
            fuelStation.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Realize qualquer limpeza ou relatórios finais, se necessário
    } // test
}
