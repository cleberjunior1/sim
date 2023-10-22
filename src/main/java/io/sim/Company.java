import java.util.ArrayList;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Company extends Thread {
    private ArrayList<Route> routesToExecute;
    private ArrayList<Route> routesInExecution;
    private ArrayList<Route> routesExecuted;
    private AlphaBank alphaBank;
    private ArrayList<Driver> drivers;
    private EnvSimulator envSimulator;

    public Company(EnvSimulator envSimulator) {
        this.routesToExecute = new ArrayList<Route>();
        this.routesInExecution = new ArrayList<Route>();
        this.routesExecuted = new ArrayList<Route>();
        this.alphaBank = new AlphaBank();
        this.drivers = new ArrayList<Driver>();
        this.envSimulator = envSimulator;
    }

    public synchronized void addRoute(Route route) {
        this.routesToExecute.add(route);
    }

    public synchronized void removeRoute(Route route) {
        this.routesToExecute.remove(route);
    }

    public synchronized Route getNextRoute() {
        if (this.routesToExecute.size() > 0) {
            Route nextRoute = this.routesToExecute.get(0);
            this.routesToExecute.remove(0);
            this.routesInExecution.add(nextRoute);
            return nextRoute;
        } else {
            return null;
        }
    }

    public synchronized void finishRoute(Route route) {
        this.routesInExecution.remove(route);
        this.routesExecuted.add(route);
    }

    public void createRoutesAndDrivers() {
        readRoutesFromXML("caminho_para_o_seu_arquivo_xml.xml");

        // Crie 100 motoristas e atribua 9 rotas a cada um de forma aleatória
        for (int i = 0; i < 100; i++) {
            Driver driver = new Driver(this.alphaBank, this); // Certifique-se de ter um construtor adequado
            drivers.add(driver);
        }

        assignRoutesToDrivers();
    }

    public void assignRoutesToDrivers() {
        // Atribuir rotas aleatoriamente aos motoristas, 9 rotas por motorista
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

    @Override
    public void run() {
        createRoutesAndDrivers();

        // Inicie os motoristas em threads separadas
        for (Driver driver : drivers) {
            driver.start();
        }

        // Aguarde o término da simulação do ambiente (SUMO)
        try {
            envSimulator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void finishRoute(Route route, double distance) {
        // Lógica para finalizar a rota
        // Substitua essa lógica pela implementação real
        System.out.println("Company: Rota " + route + " concluída. Distância percorrida: " + distance + " km");
        alphaBank.depositToCompany(distance * 3.25); // Deposite o pagamento na conta da empresa
        alphaBank.depositToFuelStation(distance * 3.25); // Deposite o pagamento na conta da estação de abastecimento
        finishRoute(route);
    }

    public void addDriver(Driver driver) {
        this.drivers.add(driver);
    }

    public void readRoutesFromXML(String xmlFilePath) {
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
                    Route route = new Route(); // Certifique-se de ter uma classe Route ou use a existente
                    // Configure a rota com base nas informações do XML
                    route.setEdges(routeEdges);
                    addRoute(route);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
