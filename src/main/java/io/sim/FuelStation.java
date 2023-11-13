package io.sim;

import java.time.Instant;
import org.json.JSONObject;

public class FuelStation implements Runnable { // Runnable é uma interface que permite que a classe seja executada em uma
                                                // thread

    private boolean isAlive = false; // variável que indica se a thread está ativa
    private JsonManager jsonMaker = new JsonManager();
    private Cryptographer encriptador = new Cryptographer();
    private SharedMemory memoriaCompartilhada = new SharedMemory();
    private JSONObject json = new JSONObject();
    private Instant timestamp;
    private double precoCombustivel;

    // Dados da conta alphabank
    private String idConta; // identificador da conta
    private double valorInicialDaConta; // saldo inicial da conta

    public FuelStation() {
        this.isAlive = true;
        this.idConta = ("Fuel_Station");
        this.valorInicialDaConta = 0;
        this.precoCombustivel = 5.87; // valor do combustível
        criarConta();

        run();
    }

    public void run() {
        while (isAlive) {
            try {
                System.out.println("Fuel Station"); // imprime na tela
                                
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }

    private void criarConta() { 
        timestamp = Instant.now(); // timestamp da criação da conta
        long timestampNanos = timestamp.getNano() + timestamp.getEpochSecond() * 1_000_000_000L;
        json = jsonMaker.JsonCriarConta(encriptador.criptografarString(idConta),
                encriptador.criptografarDouble(valorInicialDaConta), encriptador.criptografarTimestamp(timestampNanos)); // cria
                                                                                                                        // o
                                                                                                                        // json
                                                                                                                        // com
                                                                                                                        // os
                                                                                                                        // dados
                                                                                                                        // da
                                                                                                                        // conta
                                                                                                                        // para
                                                                                                                        // ser
                                                                                                                        // enviado
                                                                                                                        // para
                                                                                                                        // o
                                                                                                                        // alphabank
        memoriaCompartilhada.write(json, "CriarConta");
    }

    public double abastecer(double saldo, double FuelTank, Car carro) { // método que abastece o carro
        double preco = 0, limiteGasolina = 10, novoFuel = 0;
        carro.paraCarro();
        if ((saldo / precoCombustivel) >= limiteGasolina) { // encher o tanque
            novoFuel = 10;
            preco = precoCombustivel * 10;
        } else {
            novoFuel = saldo / precoCombustivel;
            preco = saldo;
        }
        carro.infoTanqueAbastecido(novoFuel);
        carro.liberaCarro();
        // Verifica a quantidade de gasolina, e o saldo e abastece o valor necessário
        return preco;
    }

}