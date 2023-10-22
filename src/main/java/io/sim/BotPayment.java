package io.sim;

public class BotPayment extends Thread {
    private Driver driver;
    private AlphaBank alphaBank;
    private FuelStation fuelStation;

    public BotPayment(Driver driver, AlphaBank alphaBank, FuelStation fuelStation) {
        this.driver = driver;
        this.alphaBank = alphaBank;
        this.fuelStation = fuelStation;
    }

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

    public void startPayment(double distance) {
        // Método para iniciar o pagamento com base na distância
        double payment = distance * 3.25;
        alphaBank.makePayment(driver, payment); // Realiza o pagamento do motorista
        fuelStation.addPayment(payment); // Adiciona o pagamento à FuelStation
    }
}
