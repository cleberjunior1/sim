package io.sim;

import java.util.ArrayList;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Company extends Thread {
    private ArrayList<Route> routesToExecute = new ArrayList<>();
    private ArrayList<Route> routesInExecution = new ArrayList<>();
    private ArrayList<Route> routesExecuted = new ArrayList<>();
    private AlphaBank alphaBank = new AlphaBank();
    private ArrayList<Driver> drivers = new ArrayList<>();
    private EnvSimulator envSimulator;
    private FuelStation fuelStation;
    private ArrayList<Auto> cars = new ArrayList<>();
 


    public Company() {
        fuelStation = new FuelStation(alphaBank, 2, 5.87, 10000.0);
    }


    public synchronized void addRoute(Route route) {
        routesToExecute.add(route);
    }


    public synchronized void removeRoute(Route route) {
        routesToExecute.remove(route);
    }


    public synchronized Route getNextRoute() {
        if (!routesToExecute.isEmpty()) {
            Route nextRoute = routesToExecute.remove(0);
            routesInExecution.add(nextRoute);
            return nextRoute;
        } else {
            return null;
        }
    }

    public synchronized void finishRoute(Route route, double distance) {
        routesInExecution.remove(route);
        routesExecuted.add(route);

        System.out.println("Company: Rota " + route + " concluída. Distância percorrida: " + distance + " km");
        alphaBank.depositToCompany(distance * 3.25);
        alphaBank.depositToFuelStation(distance * 3.25);
    }

    public void addDriver(Driver driver) {
        drivers.add(driver);
    }


    public void createRoutesAndDrivers() {
        readRoutesFromXML("map/map.rou.xml");

        for (int i = 0; i < 100; i++) {
            Driver driver = new Driver(alphaBank, this);
            drivers.add(driver);
        }

        assignRoutesToDrivers();
    }


    private void readRoutesFromXML(String xmlFilePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFilePath);

            NodeList routeNodes = document.getElementsByTagName("vehicle");
            for (int i = 0; i < routeNodes.getLength(); i++) {
                Node node = routeNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String routeEdges = element.getAttribute("edges");
                    Route route = new Route();
                    route.setEdges(routeEdges);
                    addRoute(route);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void assignRoutesToDrivers() {
        int routesPerDriver = 9;
        ArrayList<Route> availableRoutes = new ArrayList<>(routesToExecute);
        Random random = new Random();

        for (Driver driver : drivers) {
            for (int i = 0; i < routesPerDriver; i++) {
                if (!availableRoutes.isEmpty()) {
                    int routeIndex = random.nextInt(availableRoutes.size());
                    Route nextRoute = availableRoutes.get(routeIndex);
                    driver.addRoute(nextRoute);
                    availableRoutes.remove(routeIndex);
                }
            }
        }
    }


    public ArrayList<Driver> getDrivers() {
        return drivers;
    }
    

    public FuelStation getFuelStation() {
        return fuelStation;
    }
    

    public void addCar(Auto car) {
        cars.add(car);
    }

    @Override
    public void run() {
        createRoutesAndDrivers();

        for (Driver driver : drivers) {
            driver.start();
        }

        try {
            envSimulator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
