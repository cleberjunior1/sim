package io.sim;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Driver extends Thread {
    private List<Route> routesToExecute;
    private Route currentRoute;
    private List<Route> executedRoutes;
    private AlphaBank alphaBank;
    private Company company;

    public Driver(AlphaBank alphaBank, Company company) {
        this.routesToExecute = new ArrayList<>();
        this.executedRoutes = new ArrayList<>();
        this.alphaBank = alphaBank;
        this.company = company;
    }

    public synchronized void addRoute(Route route) {
        this.routesToExecute.add(route);
    }

    private Route getNextRoute() {
        if (!routesToExecute.isEmpty()) {
            Route nextRoute = routesToExecute.remove(0);
            return nextRoute;
        }
        return null;
    }

    private double executeRoute(Route route) {
        // Simule a execução da rota com o carro e obtenha a distância
        // Substitua essa lógica pela implementação real
        Random random = new Random();
        double distance = random.nextDouble() * 100; // Distância aleatória em km
        return distance;
    }

    @Override
    public void run() {
        while (!routesToExecute.isEmpty()) {
            currentRoute = getNextRoute();
            if (currentRoute != null) {
                System.out.println("Driver: Iniciando rota " + currentRoute);
                // Lógica para executar a rota com o carro
                double distance = executeRoute(currentRoute);
                System.out.println("Driver: Rota concluída. Distância percorrida: " + distance + " km");
                executedRoutes.add(currentRoute);
                company.finishRoute(currentRoute, distance);
            }
        }
        System.out.println("Driver: Todas as rotas foram concluídas.");
    }

    public double getDistance() {
        // Implemente a lógica para obter a distância percorrida pelo carro
        // Substitua essa lógica pela implementação real
        Random random = new Random();
        double distance = random.nextDouble() * 100; // Distância aleatória em km
        return distance;
    }
}
