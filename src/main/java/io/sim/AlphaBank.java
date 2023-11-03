package io.sim;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;

public class AlphaBank implements Runnable {

    private boolean isAlive = false;

    private ArrayList<Account> contas;
    private JSONCryptoUtil tradutor;
    private Instant ultimaLeitura;
    private Map<String, String> map = new HashMap<>();

    private MessagesJson jsonMaker = new MessagesJson();
    private JSONCryptoUtil encriptador = new JSONCryptoUtil();
    private SharedMemory memoriaCompartilhada = new SharedMemory();
    private Long timestampCriarConta;
    private Instant tempo;

    public AlphaBank() {
        this.isAlive = true;
        this.contas = new ArrayList<Account>();
        this.tradutor = new JSONCryptoUtil();
        this.tempo = Instant.now();
        timestampCriarConta = (long) 0;

        run();
    }

    public void run() {
        while (isAlive) {
            try {
                System.out.println("Thread Alphabank");
                lerArquivo();
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println(e);
            }
            // throw new UnsupportedOperationException("Unimplemented method 'run'"); -> diz
            // q o run n foi implementado
        }
    }

    public void criarConta(JSONObject arquivo) { // adicionar prefixo identificando qual tipo de conta, driver, company
                                                 // ou fuel station
        // Pega as informações encriptadas
        String idString = arquivo.get("idConta").toString();
        String StringSaldoInicial = arquivo.get("quantia").toString();
        // Descriptografa as informações
        int a = 1;
        String id = encriptador.descriptografarString(idString);
        double saldoInicial = encriptador.descriptografarDouble(StringSaldoInicial);
        // Cria a conta com os dados obtidos
        Account conta = new Account(id, saldoInicial);
        contas.add(conta);
        System.out.println("Conta cadastrada = " + conta.getAccount());
        System.out.println("Saldo da conta = " + conta.getSaldo());
    }

    public void transferencia(String idPagador, String idRecebedor, double quantia) {
        saque(idPagador, quantia);
        deposito(idRecebedor, quantia);
    }

    private void deposito(String id, double valor) {
        int index = findAccountById(id);
        contas.get(index).deposito(valor);
    }

    private void saque(String id, double valor) {
        int index = findAccountById(id);
        contas.get(index).saque(valor);
    }

    public double getSaldo(String id) {
        int index = findAccountById(id);
        return contas.get(index).getSaldo();
    }

    private int findAccountById(String id) { // ADICIONAR EXCEPTION
        boolean existe = false;
        int i;
        for (i = 0; i < contas.size(); i++) {
            if (contas.get(i).getAccount() == id) {
                existe = true;
            }
        }
        if (existe == false) {
            System.out.println("Conta não encontrada");
        }
        return i;
    }

    private void lerArquivo() {
        JSONArray jsonS = memoriaCompartilhada.read();
        for (int i = 0; i < jsonS.length(); i++) {
            JSONObject arquivo = jsonS.getJSONObject(i);
            separadorDeJsons(arquivo);
        }
    }

    private void separadorDeJsons(JSONObject arquivo) {
        switch (arquivo.get("tipo_de_requisicao").toString()) {
            // Case de criar conta
            case "CriarConta":
                Long tempoAux = verificaTempo(arquivo, timestampCriarConta);
                if (tempoAux > timestampCriarConta) {
                    timestampCriarConta = tempoAux;
                    criarConta(arquivo);
                }
                break;
        }

    }

    private Long verificaTempo(JSONObject arquivo, Long timestamp) {
        String timestampAtualStr = arquivo.get("timestamp").toString();
        Long timestampAtual = encriptador.descriptografarTimestamp(timestampAtualStr);
        if (timestampAtual > timestamp) {
            timestamp = timestampAtual;
            System.out.println("CRIA CONTA");
        } else {
            System.out.println("NAO CRIA CONTA");
        }
        return timestamp;
    }
}