package io.sim;

import java.time.Instant;
import java.util.ArrayList;

public class Account extends Thread {
    private String idAccount = new String();
    private double saldo;
    private ArrayList <String> registroAtividades = new ArrayList<String>();
    private Instant timestamp;

    public Account (String conta, double saldoInicial){
        this.idAccount = conta;
        this.saldo = saldoInicial;
        timestamp = Instant.now();
        long timestampNanos = timestamp.getNano() + timestamp.getEpochSecond() * 1_000_000_000L;
       registrarOperação(timestampNanos + "# Conta " + conta + " criada, saldo inicial de " + saldoInicial);
    }

    public void run() {
        while (isAlive()) {
            try {
                System.out.println("Thread_account");
                Thread.sleep(1000);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    public String getAccount() {
        return this.idAccount;
    }

    public double getSaldo() {
        return saldo;
    }

    public void deposito(double valor) {
        this.saldo += valor;
    }

    public void saque(double valor) {
        if (valor > saldo) {
            System.out.println("A conta " + idAccount + "não possui o valor desejado para sacar!\nSaldo disponível para retirada: " + getSaldo());
        }else{
            this.saldo-=valor;            
        } 
    }
    
    private void registrarOperação(String registro){
        this.registroAtividades.add(registro);
    }
}