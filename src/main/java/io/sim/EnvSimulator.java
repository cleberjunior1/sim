package io.sim;

import java.io.IOException;

import de.tudresden.sumo.objects.SumoColor;
import it.polito.appeal.traci.SumoTraciConnection;

public class EnvSimulator extends Thread {
    private SumoTraciConnection sumo;
    private Company company;

    public EnvSimulator(Company company) {
        this.company = company;

    }

    @Override
    public void run() {
        try {
            // Configuração do SUMO
            String sumo_bin = "sumo-gui";
            String config_file = "map/map.sumo.cfg";

            // Conexão com o SUMO
            this.sumo = new SumoTraciConnection(sumo_bin, config_file);
            sumo.addOption("start", "1"); // Iniciar automaticamente ao mostrar a GUI
            sumo.addOption("quit-on-end", "1"); // Fechar automaticamente ao terminar

            sumo.runServer(12345);

            // Configuração do itinerário e veículo
            Itinerary i1 = new Itinerary("data/dados2.xml", "0");
            

            if (i1.isOn()) {
                int numCars = 100; // Defina o número de carros que deseja criar
                int fuelType = 2; // 2 representa gasolina
                int fuelPreferential = 1; // 1 representa preferência por algum tipo de combustível
                double fuelPrice = 3.40; // Defina o preço do combustível conforme necessário


                SumoColor green = new SumoColor(0, 255, 0, 126);


                for (int i = 1; i <= numCars; i++) {
                    String carId = "CAR" + i;
                    


                    Auto auto = new Auto(true, carId, green, "D1", sumo, 500, fuelType, fuelPreferential, fuelPrice, 1, 1);
                    company.addCar(auto); // Adicione o carro à lista de carros da Company
                    TransportService tS1 = new TransportService(true, carId, i1, auto, sumo);
                    tS1.start();
                    Thread.sleep(5000);
                    auto.start();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
