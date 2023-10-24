package io.sim;

import java.util.ArrayList;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents a company that manages routes, drivers, fuel stations, and cars.
 */
/**
 * The Company class represents a company that manages a fleet of cars and drivers, and executes routes.
 * It contains methods for adding and removing routes, getting the next route to execute, finishing a route, adding a driver, 
 * creating routes and drivers from an XML file, assigning routes to drivers, getting the list of drivers, getting the fuel station, 
 * adding a car, and running the simulation.
 */
public class Company extends Thread {
    private ArrayList<Route> routesToExecute = new ArrayList<>();
    private ArrayList<Route> routesInExecution = new ArrayList<>();
    private ArrayList<Route> routesExecuted = new ArrayList<>();
    private AlphaBank alphaBank = new AlphaBank();
    private ArrayList<Driver> drivers = new ArrayList<>();
    private EnvSimulator envSimulator;
    private FuelStation fuelStation;
    private ArrayList<Auto> cars = new ArrayList<>();
    private JSONCryptoUtil jsonCryptoUtil;

    /**
     * Constructs a new Company object with a FuelStation object.
     */
    public Company() {
        fuelStation = new FuelStation(alphaBank, 2, 5.87, 10000.0);
    }

    /**
     * Adds a route to the list of routes to execute.
     * @param route The route to add.
     */
    public synchronized void addRoute(Route route) {
        routesToExecute.add(route);
    }

    /**
     * Removes a route from the list of routes to execute.
     * @param route The route to remove.
     */
    public synchronized void removeRoute(Route route) {
        routesToExecute.remove(route);
    }

    /**
     * Gets the next route to execute.
     * @return The next route to execute, or null if there are no more routes to execute.
     */
    public synchronized Route getNextRoute() {
        if (!routesToExecute.isEmpty()) {
            Route nextRoute = routesToExecute.remove(0);
            routesInExecution.add(nextRoute);
            return nextRoute;
        } else {
            return null;
        }
    }

    /**
     * Finishes a route and updates the company's AlphaBank account.
     * @param route The route that was finished.
     * @param distance The distance that was traveled.
     */
    public synchronized void finishRoute(Route route, double distance) {
        routesInExecution.remove(route);
        routesExecuted.add(route);

        System.out.println("Company: Rota " + route + " concluída. Distância percorrida: " + distance + " km");
        alphaBank.depositToCompany(distance * 3.25);
        alphaBank.depositToFuelStation(distance * 3.25);
    }

    /**
     * Adds a driver to the list of drivers.
     * @param driver The driver to add.
     */
    public void addDriver(Driver driver) {
        drivers.add(driver);
    }

    /**
     * Reads routes from an XML file and adds them to the list of routes to execute.
     * @param xmlFilePath The path to the XML file.
     */
    public void createRoutesAndDrivers() {
        readRoutesFromXML("map/map.rou.xml");

        for (int i = 0; i < 100; i++) {
            Driver driver = new Driver(alphaBank, this);
            drivers.add(driver);
        }

        assignRoutesToDrivers();
    }

    /**
     * Reads routes from an XML file and adds them to the list of routes to execute.
     * @param xmlFilePath The path to the XML file.
     */
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

    /**
     * Assigns routes to drivers.
     */
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

    /**
     * Gets the list of drivers.
     * @return The list of drivers.
     */
    public ArrayList<Driver> getDrivers() {
        return drivers;
    }
    
    /**
     * Gets the fuel station.
     * @return The fuel station.
     */
    public FuelStation getFuelStation() {
        return fuelStation;
    }
    
    /**
     * Adds a car to the list of cars.
     * @param car The car to add.
     */
    public void addCar(Auto car) {
        cars.add(car);
    }

    /**
     * Runs the simulation.
     */
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
