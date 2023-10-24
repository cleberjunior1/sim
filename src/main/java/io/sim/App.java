package io.sim;

/**
 * The App class is the main entry point for the simulation application.
 */
public class App {

    /**
     * The main method initializes the simulation environment and starts the simulation.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        
        AlphaBank alphaBank = new AlphaBank();
        double initialFuelStock = 1000.0; 

        FuelStation fuelStation = new FuelStation(alphaBank, 2, 5.87, initialFuelStock); 

        Company company = new Company();

        EnvSimulator envSimulator = new EnvSimulator(company);

      
        fuelStation.start();

        
        company.start();

        
        alphaBank.start();

        envSimulator.start();

       
        try {
            company.join();
            fuelStation.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        
    }
}
