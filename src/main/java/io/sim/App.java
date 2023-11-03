package io.sim;

/**
 * A classe App é o método main que inicializa as classes principais do projeto.
 */
public class App {

    /**
     * Inicialização das classes principais do projeto.
     */
    public static void main(String[] args) {
        
        EnvSimulator envSimulator = new EnvSimulator(); // responsável por fazer a comunicação com sumo e iniciar a simulação, as rotas e os drivers
        AlphaBank alphaBank = new AlphaBank(); // gerencia toda a parte de pagamento
        FuelStation fuelStation = new FuelStation(); // representa o posto de combustível
        Company company = new Company(envSimulator); // representa a companhia (mobility company) 
        

        // Inicialização das threads
        // Lembrando que tem que ser por run() porque se trata de runnable
        fuelStation.run();        
        company.run();        
        alphaBank.run();
       
       
    }
}
