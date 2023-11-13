package io.sim;

import java.net.ServerSocket; // classe que representa o servidor. Futura implementação
import java.util.ArrayList; 

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * A classe App é o método main que inicializa as classes principais do projeto.
 */
public class App {

    /**
     * Inicialização das classes principais do projeto.
     */
    public static void main(String[] args) {
        
        EnvSimulator envSimulator = new EnvSimulator(); // responsável por fazer a comunicação com sumo e iniciar a simulação, as rotas e os drivers
        AlphaBank alphaBank = new AlphaBank(); // gerencia toda a parte de pagamento
        FuelStation fuelStation = new FuelStation(); // representa o posto de combustível
        Company company = new Company(envSimulator); // representa a companhia (mobility company) 
        
        Thread envThread = new Thread(envSimulator); // cria as threads
        Thread fuelStationThread = new Thread(fuelStation); 
        Thread companyThread = new Thread(company);
        Thread alphaBankThread = new Thread(alphaBank);

        envThread.start(); // inicia as threads
        fuelStationThread.start();  // Ao iniciar as threads, o método run() de cada classe é executado
        companyThread.start();
        alphaBankThread.start();


        try { // espera as threads terminarem de executar
            envThread.join();
            fuelStationThread.join();
            companyThread.join();
            alphaBankThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 

        // Inicialização das threads
        // Lembrando que tem que ser por run() porque se trata de runnable

       // Comentar sobre a utilização do método join aqui
       // O método join() faz com que a thread que o chamou espere a thread que ele chamou terminar de executar
       // Isso é necessário para que o programa não termine antes das threads terminarem de executar


        try{
        //Cria os objetos necessário para instânciar o servidor
        
        JLabel lblMessage = new JLabel("Porta do Servidor:");
        JTextField txtPorta = new JTextField("12345");
        Object[] texts = {lblMessage, txtPorta };
        JOptionPane.showMessageDialog(null, texts);
        server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
        clientes = new ArrayList<BufferedWriter>();
        JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+
        txtPorta.getText());

            while(true){
            System.out.println("Aguardando conexão...");
            Socket con = server.accept();
            System.out.println("Cliente conectado...");
            Thread t = new Servidor(con);
                t.start();
            }

        }catch (Exception e) {

            e.printStackTrace();
        }
        
 }// Fim do método main
}


