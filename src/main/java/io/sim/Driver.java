package io.sim;

import java.util.ArrayList;
import java.util.List;

public class Driver extends Thread {
    private List<Route> routesToExecute;
    private AlphaBank alphaBank;
    private Company company;
    private double totalDistance; // Variável para acompanhar a distância total percorrida
    private Car car; // Adicione uma instância da classe Car

    public Driver(AlphaBank alphaBank, Company company) {
        this.routesToExecute = new ArrayList<>();
        this.alphaBank = alphaBank;
        this.company = company;
        this.totalDistance = 0.0; // Inicialize a distância total como zero
    }

    public synchronized void addRoute(Route route) {
        this.routesToExecute.add(route);
    }

    private Route getNextRoute() {
        if (!routesToExecute.isEmpty()) {
            return routesToExecute.remove(0);
        }
        return null;
    }

    private double executeRoute(Route route) {
        // Lógica para executar a rota com o carro e obter a distância
        double distance = 0.0; // Substitua isso pela lógica real
        return distance;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public void run() {
        while (true) {
            Route currentRoute = getNextRoute();
            if (currentRoute == null) {
                break; // Sai do loop se não houver mais rotas
            }

            System.out.println("Driver: Iniciando rota " + currentRoute);
            double distance = executeRoute(currentRoute);
            System.out.println("Driver: Rota concluída. Distância percorrida: " + distance + " km");
            totalDistance += distance; // Atualize a distância total
            company.finishRoute(currentRoute, distance);
        }

        System.out.println("Driver: Todas as rotas foram concluídas.");
    }

    public double getDistance() {
        return totalDistance; // Obtenha a distância total percorrida pelo motorista
    }
}
