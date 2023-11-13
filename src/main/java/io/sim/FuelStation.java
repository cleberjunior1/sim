package io.sim;

import java.time.Instant;
import org.json.JSONObject;

public class FuelStation implements Runnable {

    private boolean isAlive = false;
    private JsonManager jsonMaker = new JsonManager();
    private Cryptographer encriptador = new Cryptographer();
    private SharedMemory memoriaCompartilhada = new SharedMemory();
    private JSONObject json = new JSONObject();
    private Instant timestamp;
    private double precoCombustivel;

    // Dados da conta alphabank
    private String idConta;
    private double valorInicialDaConta;

    public FuelStation() {
        this.isAlive = true;
        this.idConta = ("Fuel_Station");
        this.valorInicialDaConta = 0;
        this.precoCombustivel = 5.87;
        criarConta();

        run();
    }

    public void run() {
        while (isAlive) {
            try {
                System.out.println("Fuel Station");
                // funções a serem processadas
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println(e);
            }
            // throw new UnsupportedOperationException("Unimplemented method 'run'"); -> diz
            // q o run n foi implementado
        }
    }

    private void criarConta() {
        timestamp = Instant.now();
        long timestampNanos = timestamp.getNano() + timestamp.getEpochSecond() * 1_000_000_000L;
        json = jsonMaker.JsonCriarConta(encriptador.criptografarString(idConta),
                encriptador.criptografarDouble(valorInicialDaConta), encriptador.criptografarTimestamp(timestampNanos));
        memoriaCompartilhada.write(json, "CriarConta");
    }

    public double abastecer(double saldo, double FuelTank, Car carro) {
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