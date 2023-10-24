package io.sim;

/**
 * This class represents a thread that handles the payment process for a driver.
 */
public class BotPayment extends Thread {
    private Driver driver;
    private AlphaBank alphaBank;
    private FuelStation fuelStation;

    /**
     * Constructor for the BotPayment class.
     * @param driver The driver that will be making the payment.
     * @param alphaBank The bank that will be handling the payment.
     * @param fuelStation The fuel station that will receive the payment.
     */
    public BotPayment(Driver driver, AlphaBank alphaBank, FuelStation fuelStation) {
        this.driver = driver;
        this.alphaBank = alphaBank;
        this.fuelStation = fuelStation;
    }

    /**
     * The run method for the BotPayment thread.
     * This method calculates the payment based on the driver's distance and makes the payment to the bank.
     * It also adds the payment to the fuel station's balance.
     */
    @Override
    public void run() {
        while (true) {
            double distance = driver.getDistance();
            double payment = distance * 3.25;
            alphaBank.makePayment(driver, payment); // Realiza o pagamento do motorista
            fuelStation.addPayment(payment); // Adiciona o pagamento à FuelStation
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to start the payment process based on a given distance.
     * @param distance The distance that will be used to calculate the payment.
     */
    public void startPayment(double distance) {
        double payment = distance * 3.25;
        alphaBank.makePayment(driver, payment); // Realiza o pagamento do motorista
        fuelStation.addPayment(payment); // Adiciona o pagamento à FuelStation
    }
}
