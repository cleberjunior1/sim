package io.sim;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Company implements Runnable {
    // Deve ser uma thread
    // deve conter um conjunto de rotas -> FEITO
    // Deve ser um servidor para carros
    // Deve ser um cliente de alphaBank -> deve ter uma conta no alphaBank
    // Criar uma Classe BotPayment (Thread)
    // gerar xlsl de relatório (xlsl é sugestão minha)

    private boolean isAlive = false;

    private double precoPkm;
    private ArrayList<Route> rotasAseremExecutadas;
    private ArrayList<Route> rotasEmExecucao;
    private ArrayList<Route> rotasExecutadas;

    private Instant timestamp;
    private JsonManager jsonMaker = new JsonManager();
    private Cryptographer encriptador = new Cryptographer();
    private JSONObject json = new JSONObject();
    private SharedMemory memoriaCompartilhada = new SharedMemory();
    private BotPayment botDePagamentos;

    private final String caminhoPastaRotas = "map/map.rou.xml";

    // Dados da conta alphabank
    private String idConta;
    private double valorInicialDaConta;

    private ArrayList<Driver> motoristas = new ArrayList<Driver>();
    private int nRotasPorPiloto = 0;
    private int indexControleDistribuicaoRotas;

    private ArrayList<JSONObject> solicitacoesRotas = new ArrayList<JSONObject>();

    private EnvSimulator simulador;

    // private String idItinerario;

    public Company(EnvSimulator ev) {
        this.isAlive = true;
        this.precoPkm = 3.25;
        this.idConta = "Company";
        this.valorInicialDaConta = 100.0;
        this.indexControleDistribuicaoRotas = 0;
        this.simulador = ev;

        getRoutes(); // Pega as rotas
        limparArquivoDeRotas();
        createDriver();
        distribuirRotas();
        // criar conta no banco
        criarConta();

        botDePagamentos = new BotPayment();
        //simulador.start();
        run();
    }

    private void distribuirRotas() {
        int acumulador = 0, indexDriver = 0;
        ArrayList<Route> edges = new ArrayList<Route>();
        for (int i = 0; i < rotasAseremExecutadas.size(); i++) {
            if (acumulador < nRotasPorPiloto) {
                edges.add(rotasAseremExecutadas.get(i));
            } else if (acumulador == (nRotasPorPiloto - 1)) {
                // Atribui
                Driver motoristaAtual = motoristas.get(indexDriver);
                motoristaAtual.setRoute(edges);
            } else {
                edges.clear();
                acumulador = 0;
            }
        }
    }

    private void limparArquivoDeRotas() {
    }

    private void createDriver() {
        // Verifica o número de rotas
        for (int i = 0; i < 100; i++) {
            motoristas.add(i, new Driver("Driver_" + i, simulador));
        }

        simulador.setDrivers(motoristas);

        int numeroDeRotas = rotasAseremExecutadas.size();
        int novoNumeroDeRotas = numeroDeRotas - numeroDeRotas % 100;
        List<Route> subLista = rotasAseremExecutadas.subList(0, novoNumeroDeRotas);
        this.rotasAseremExecutadas.clear(); // Limpa a lista original
        this.rotasAseremExecutadas.addAll(subLista); // Copia a sublista para a lista original
        this.nRotasPorPiloto = rotasAseremExecutadas.size() / 100;
        // return rotasAseremExecutadas.size()/100;
    }

    private void getRoutes() {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder construtor = builderFactory.newDocumentBuilder();
            Document doc = construtor.parse(caminhoPastaRotas);
            NodeList lista = doc.getElementsByTagName("vehicle");
            for (int i = 0; i < lista.getLength(); i++) {
                this.rotasAseremExecutadas.add(new Route(Integer.toString(i)));
            }
        } catch (SAXException | IOException | DOMException e) {
            e.printStackTrace(); // ou tratamento adequado
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void run() {
        // Processos iniciais...
        System.out.println("Company");
        while (isAlive) {
            try {
                // System.out.println("Thread Company");
                verificarSolicitacoes();
                if (solicitacoesRotas.size() > 0) {
                    for (int i = 0; i < solicitacoesRotas.size(); i++) {
                        if (solicitacoesRotas.get(i).get("statusSolicitacao") == "nao_atendida") {
                            String driverSolicitante = (String) solicitacoesRotas.get(i).get("idDriverSolicitante");
                            atenderSolicitacao(encriptador.descriptografarString(driverSolicitante));
                            solicitacoesRotas.get(i).put("statusSolicitacao", "atendida"); // Indica que a rota já foi
                                                                                           // atribuída
                        }
                    }
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    private void atenderSolicitacao(String driverSolicitante) {
        for (int i = 0; i < motoristas.size(); i++) {
            Driver motoristaSolicitante = (motoristas.get(i));
            if (motoristaSolicitante.getId() == driverSolicitante) {
                ArrayList<Route> rotasAEnviar = new ArrayList<Route>();
                for (int j = 0; j < nRotasPorPiloto; j++) {
                    rotasAEnviar.add(rotasAseremExecutadas.get(indexControleDistribuicaoRotas + i));
                }
                indexControleDistribuicaoRotas += nRotasPorPiloto; // Atualiza o indexador das rotas distribuidas
                motoristaSolicitante.setRoute(rotasAseremExecutadas);
            }
        }
    }

    private void verificarSolicitacoes() {
        JSONArray jsonS = memoriaCompartilhada.read();
        for (int i = 0; i < jsonS.length(); i++) {
            JSONObject arquivo = jsonS.getJSONObject(i);
            separadorDeJsons(arquivo);
        }
    }

    private void separadorDeJsons(JSONObject arquivo) {
        switch (arquivo.get("tipo_de_requisicao").toString()) {
            // Case de criar conta
            case "2":
                boolean novaSolicitacao = false;
                for (int i = 0; i < solicitacoesRotas.size(); i++) {
                    if (arquivo.get("idDriverSolicitante") == solicitacoesRotas.get(i).get("idDriverSolicitante")) {
                        novaSolicitacao = true;
                    }
                }
                if (novaSolicitacao == true) {
                    solicitacoesRotas.add(arquivo);
                }
                break;
        }

    }

    private void criarConta() {
        timestamp = Instant.now();
        long timestampNanos = timestamp.getNano() + timestamp.getEpochSecond() * 1_000_000_000L;
        json = jsonMaker.JsonCriarConta(encriptador.criptografarString(idConta),
                encriptador.criptografarDouble(valorInicialDaConta), encriptador.criptografarTimestamp(timestampNanos));
        memoriaCompartilhada.write(json, "CriarConta");
    }

    public void addRoute(Route route) {
        this.rotasAseremExecutadas.add(route);
    }

    public ArrayList<Route> getCurrentRoute() {
        return rotasEmExecucao;
    }

    public ArrayList<Route> getExecutedRoutes() {
        return rotasExecutadas;
    }

    class BotPayment {

        private boolean isAlive = false;
        private Instant timestamp;
        private JsonManager jsonMaker = new JsonManager();
        private Cryptographer encriptador = new Cryptographer();
        private JSONObject json = new JSONObject();
        private SharedMemory memoriaCompartilhada = new SharedMemory();
        private float kmPago;
        private float kmRodado;
        private boolean pagarPosto;
        private Map<String, Double> pagamentosPendentes = new HashMap<String, Double>();
        // public static float kmAtual = 0;;

        private String idConta;
        private double valorAPagar;

        public BotPayment() {
            this.isAlive = true;
            this.idConta = "Company";
            this.pagarPosto = false;
            getRoutes();
            run();
        }

        // Fazer fila de pagamentos para os drivers

        public void run() {
            // Processos iniciais...
            while (isAlive) {
                try {
                    // System.out.println("Thread botpayment");
                    // adicionar verificação de km percorrido
                    if (pagamentosPendentes.size() > 0) {
                        pay();
                        kmPago = kmRodado;
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }

        public void setPagarPosto(boolean pagarPosto, double quantia) {
            this.pagarPosto = pagarPosto;
            this.valorAPagar = quantia;
        }

        public void pay() {
            timestamp = Instant.now();
            long timestampNanos = timestamp.getNano() + timestamp.getEpochSecond() * 1_000_000_000L;
            json = jsonMaker.JsonTransferencia("4", encriptador.criptografarString(idConta),
                    encriptador.criptografarString("idCarro"), encriptador.criptografarDouble(valorAPagar),
                    encriptador.criptografarTimestamp(timestampNanos));
            memoriaCompartilhada.write(json, "6");
            pagarPosto = false;
            valorAPagar = 0;
        }
    }

}