package io.sim; // Inclui a classe na pasta io.sim

// Importa as bibliotecas necessárias
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

// Aqui implementei Runnable para poder especificar o que o método run() vai fazer
// Como consequência, não podemos usar o método join() para esperar a thread terminar
// A classe Company é responsável por gerenciar os drivers e as rotas
// Ela também é responsável por criar a conta da companhia no banco
// Ela também é responsável por receber as solicitações de rotas e distribuir as rotas para os drivers
// Ela também é responsável por verificar se há pagamentos pendentes e realizar os pagamentos
// Ela também é responsável por verificar se há solicitações de rotas pendentes e atender as solicitações

public class Company implements Runnable {

    // Variável que indica se a classe está ativa
    // Runnable não tem isAlive, somente thread, por isso utilizo uma variável booleana
    private boolean isAlive = false;

    // Variável que indica o preço por km
    private double precoPkm;

    // Variáveis responsaveis pelas rotas, a serem executadas, em execução e já executadas
    private ArrayList<Route> rotasAseremExecutadas;
    private ArrayList<Route> rotasEmExecucao;
    private ArrayList<Route> rotasExecutadas;
    

    private Instant timestamp;
    private JsonManager jsonMaker = new JsonManager();
    private Cryptographer encriptador = new Cryptographer();
    private JSONObject json = new JSONObject();
    private SharedMemory memoriaCompartilhada = new SharedMemory(); // Memória compartilhada para comunicação entre as
                                                                   // classes. Não será necessária após a criação do chat por socket
    private BotPayment botDePagamentos; // Intstancia Bot responsável por fazer os pagamentos

    private final String caminhoPastaRotas = "map/map.rou.xml";

    // Dados da conta alphabank
    private String idConta;
    private double valorInicialDaConta;

    private ArrayList<Driver> motoristas = new ArrayList<Driver>(); // Lista de motoristas
    private int nRotasPorPiloto = 0; // Número de rotas por piloto iniciado em 9
    private int indexControleDistribuicaoRotas;

    private ArrayList<JSONObject> solicitacoesRotas = new ArrayList<JSONObject>(); // Lista de solicitações de rotas

    private EnvSimulator simulador; // Instância do simulador

    // private String idItinerario;

    // Contrutor da classe Company
    public Company(EnvSimulator ev) {
        this.isAlive = true; // Indica que a classe está ativa
        this.precoPkm = 3.25; // Preço por km
        this.idConta = "Company"; // Id da conta da companhia
        this.valorInicialDaConta = 100.0; // Valor inicial da conta da companhia
        this.indexControleDistribuicaoRotas = 0; // Indexador para controlar a distribuição das rotas
        this.simulador = ev; // Instância do simulador

        getRoutes(); // Pega as rotas
        limparArquivoDeRotas(); // Limpa o arquivo de rotas
        createDriver(); // Cria os drivers
        distribuirRotas(); // Distribui as rotas para os drivers
        // criar conta no banco
        criarConta(); // Cria a conta da companhia no banco

        botDePagamentos = new BotPayment();
        //simulador.start(); // thread do simulador
        run(); 
    }

    private void distribuirRotas() { // Distribui as rotas para os drivers
        int acumulador = 0, indexDriver = 0; // Acumulador para controlar o número de rotas por piloto e indexador para
                                             // controlar o motorista
        ArrayList<Route> edges = new ArrayList<Route>();
        for (int i = 0; i < rotasAseremExecutadas.size(); i++) { // Percorre a lista de rotas a serem executadas
            if (acumulador < nRotasPorPiloto) {
                edges.add(rotasAseremExecutadas.get(i));
            } else if (acumulador == (nRotasPorPiloto - 1)) { // Verifica se o acumulador é igual ao número de rotas
                                                              // por piloto
                // Atribui
                Driver motoristaAtual = motoristas.get(indexDriver);
                motoristaAtual.setRoute(edges);
            } else {
                edges.clear();
                acumulador = 0;
            }
        }
    }

    private void limparArquivoDeRotas() { // Não implementado
        // Limpa o arquivo de rotas
    }

    private void createDriver() {
        for (int i = 0; i < 100; i++) { // Cria 100 motoristas conforme especificado
            motoristas.add(i, new Driver("Driver_" + i, simulador));
        }

        simulador.setDrivers(motoristas); // Passa a lista de motoristas para o simulador (não está funcionando, precisa de ajustes em envSimulator)

        int numeroDeRotas = rotasAseremExecutadas.size();
        int novoNumeroDeRotas = numeroDeRotas - numeroDeRotas % 100;
        List<Route> subLista = rotasAseremExecutadas.subList(0, novoNumeroDeRotas);
        this.rotasAseremExecutadas.clear(); // Limpa a lista original
        this.rotasAseremExecutadas.addAll(subLista); // Copia a sublista para a lista original
        this.nRotasPorPiloto = rotasAseremExecutadas.size() / 100;
        // return rotasAseremExecutadas.size()/100;
    }

    /**
     * Parses an XML file containing vehicle routes and adds them to the list of routes to be executed.
     * Uses a DocumentBuilder to parse the XML file and a Document object to represent the parsed document.
     * @throws SAXException if there is an error parsing the XML file
     * @throws IOException if there is an error reading the XML file
     * @throws DOMException if there is an error creating the DocumentBuilder
     */
    private void getRoutes() {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance(); // Cria um objeto
                                                                                           // DocumentBuilderFactory
            /*Esse trecho de código cria um objeto DocumentBuilderFactory usando o método estático newInstance() da classe DocumentBuilderFactory.
            O objeto criado é usado para criar objetos DocumentBuilder, que são usados para analisar documentos XML. O método newInstance() retorna
            uma nova instância de DocumentBuilderFactory que pode ser usada para criar objetos DocumentBuilder.Esse trecho de código cria um objeto
            DocumentBuilderFactory usando o método estático newInstance() da classe DocumentBuilderFactory. O objeto criado é usado para criar objetos
            DocumentBuilder, que são usados para analisar documentos XML. O método newInstance() retorna uma nova instância de DocumentBuilderFactory que
            pode ser usada para criar objetos DocumentBuilder. */
            DocumentBuilder construtor = builderFactory.newDocumentBuilder();
            Document doc = construtor.parse(caminhoPastaRotas);
            NodeList lista = doc.getElementsByTagName("vehicle");
            for (int i = 0; i < lista.getLength(); i++) {
                this.rotasAseremExecutadas.add(new Route(Integer.toString(i)));
            }
        } catch (SAXException | IOException | DOMException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes the main logic of the Company class in a separate thread.
     * It checks for pending route requests and assigns them to available drivers.
     * The method runs indefinitely until the isAlive flag is set to false.
     */
    public void run() { // Método run() da classe Company
        System.out.println("Run na classe Company");
        while (isAlive) { // Enquanto a classe estiver ativa
            try {
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
                Thread.sleep(1000); // Espera 1 segundo
            } catch (Exception e) { // Tratamento de erros
                System.out.println("Erro na thread Company"); // Imprime o erro
            }
        }
    }

    private void atenderSolicitacao(String driverSolicitante) { // Atende a solicitação de rota
        for (int i = 0; i < motoristas.size(); i++) {
            Driver motoristaSolicitante = (motoristas.get(i)); // Pega o motorista solicitante
            if (motoristaSolicitante.getId() == driverSolicitante) { // Verifica se o motorista solicitante é o mesmo
                                                                     // que o motorista atual
                ArrayList<Route> rotasAEnviar = new ArrayList<Route>();
                for (int j = 0; j < nRotasPorPiloto; j++) {         // Pega as rotas a serem executadas
                    rotasAEnviar.add(rotasAseremExecutadas.get(indexControleDistribuicaoRotas + i));
                }
                indexControleDistribuicaoRotas += nRotasPorPiloto; // Atualiza o indexador das rotas distribuidas
                motoristaSolicitante.setRoute(rotasAseremExecutadas); // Atribui as rotas ao motorista solicitante
            }
        }
    }

    // Aqui está utilizando memória compartilhada, mas pretendo utilizar oscket no futuro
    private void verificarSolicitacoes() {
        JSONArray jsonS = memoriaCompartilhada.read(); // Lê o arquivo de solicitações
        for (int i = 0; i < jsonS.length(); i++) {
            JSONObject arquivo = jsonS.getJSONObject(i);
            separadorDeJsons(arquivo);
        }
    }

    private void separadorDeJsons(JSONObject arquivo) {
        switch (arquivo.get("tipo_de_requisicao").toString()) { // Verifica o tipo de requisição
            // Case de criar conta
            case "1":
                idConta = arquivo.get("idConta").toString(); // Pega o id da conta
                valorInicialDaConta = Double.parseDouble(arquivo.get("valorInicialDaConta").toString()); // Pega o valor
                                                                                                         // inicial da
                                                                                                         // conta
                break;            
            case "2":
                boolean novaSolicitacao = false;
                for (int i = 0; i < solicitacoesRotas.size(); i++) {
                    if (arquivo.get("idDriverSolicitante") == solicitacoesRotas.get(i).get("idDriverSolicitante")) { // Verifica se a solicitação já existe
                        novaSolicitacao = true; // Indica que a solicitação já existe
                    }
                }
                if (novaSolicitacao == true) {
                    solicitacoesRotas.add(arquivo); // Adiciona a solicitação na lista de solicitações
                }
                break;
        }

    }

    private void criarConta() {
        timestamp = Instant.now(); // Pega o timestamp para a criação da conta
        long timestampNanos = timestamp.getNano() + timestamp.getEpochSecond() * 1_000_000_000L; // Converte o timestamp
                                                                                                 // para nanossegundos
        json = jsonMaker.JsonCriarConta(encriptador.criptografarString(idConta),
                encriptador.criptografarDouble(valorInicialDaConta), encriptador.criptografarTimestamp(timestampNanos)); // Cria
                                                                                                                        // o
                                                                                                                        // json
                                                                                                                        // para
                                                                                                                        // criar
                                                                                                                        // a
                                                                                                                        // conta
        memoriaCompartilhada.write(json, "CriarConta"); // Escreve o json no arquivo de memória compartilhada
    }

    public void addRoute(Route route) { // Adiciona uma rota
        this.rotasAseremExecutadas.add(route); // Adiciona a rota na lista de rotas a serem executadas
    }

    public ArrayList<Route> getCurrentRoute() {
        return rotasEmExecucao; // Retorna as rotas em execução
    }

    public ArrayList<Route> getExecutedRoutes() {
        return rotasExecutadas; // Retorna as rotas executadas
    }

    class BotPayment { // Bot responsável por fazer os pagamentos
        // Antes eu estava utilizando de forma spearada, mas preferi centralizar
        private boolean isAlive = false; // Variável que indica se a classe está ativa
        private Instant timestamp; // Variável que indica o timestamp
        // O timestamp é importante para acesso e pagamento de contas no banco pois é a forma de identificar a conta a ser paga e o momento do pagamento
        // Importante para: Rastreamento de pagamentos, segurança, Conciliação de contas e auditoria
        private JsonManager jsonMaker = new JsonManager(); // Instância do JsonManager
        private Cryptographer encriptador = new Cryptographer(); // Instância do Cryptographer
        private JSONObject json = new JSONObject(); // Instância do JSONObject
        private SharedMemory memoriaCompartilhada = new SharedMemory(); // Instância da memória compartilhada
        private float kmPago; // Variável que indica a quantidade de km paga
        private float kmRodado; // Variável que indica a quantidade de km rodado
        private boolean pagarPosto; // Variável que indica se é para pagar o posto
        private Map<String, Double> pagamentosPendentes = new HashMap<String, Double>(); // Mapa que indica os pagamentos pendentes
        // public static float kmAtual = 0;;

        private String idConta;
        private double valorAPagar;

        public BotPayment() { // Construtor da classe BotPayment
            this.isAlive = true;
            this.idConta = "Company";
            this.pagarPosto = false;
            getRoutes();
            run();
        }

        // Fazer fila de pagamentos para os drivers

        public void run() { // Método run() da classe BotPayment
            while (isAlive) { // Enquanto a classe estiver ativa
                try {
                    if (pagamentosPendentes.size() > 0) { // Verifica se há pagamentos pendentes
                        pay(); // Paga
                        kmPago = kmRodado; // Atualiza a quantidade de km paga
                    }
                    Thread.sleep(1000); // Espera 1 segundo
                } catch (Exception e) {
                    System.out.println("Erro na thread BotPayment"); // Imprime o erro
                }
            }
        }

        public void setPagarPosto(boolean pagarPosto, double quantia) { // Método que indica se é para pagar o posto
            this.pagarPosto = pagarPosto; // Atualiza a variável pagarPosto
            this.valorAPagar = quantia; // Atualiza o valor a pagar
        }

        public void pay() { // Método que paga
            timestamp = Instant.now(); // Pega o timestamp
            long timestampNanos = timestamp.getNano() + timestamp.getEpochSecond() * 1_000_000_000L; // Converte o
                                                                                                     // timestamp para
                                                                                                     // nanossegundos
            json = jsonMaker.JsonTransferencia("4", encriptador.criptografarString(idConta),
                    encriptador.criptografarString("idCarro"), encriptador.criptografarDouble(valorAPagar),
                    encriptador.criptografarTimestamp(timestampNanos)); // Cria o json para a transferência
            memoriaCompartilhada.write(json, "6"); // Escreve o json no arquivo de memória compartilhada
            pagarPosto = false; // Indica que não é para pagar o posto
            valorAPagar = 0; // Zera o valor a pagar
        }
    }

}