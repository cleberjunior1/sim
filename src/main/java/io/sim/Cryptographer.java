package io.sim;

import org.jasypt.util.text.BasicTextEncryptor;

public class Cryptographer {
    // Classe para criptografar e desencriptografar dados de mensagens trocadas
    // entre as classes
    private String cifra = "chave_secreta";

    public String criptografarString(String dados) {
        BasicTextEncryptor texto = new BasicTextEncryptor();
        texto.setPasswordCharArray(cifra.toCharArray());
        String dadoCriptografado = texto.encrypt(dados);
        return dadoCriptografado;
    }

    public String criptografarDouble(double dados) {
        BasicTextEncryptor texto = new BasicTextEncryptor();
        texto.setPasswordCharArray(cifra.toCharArray());
        String dadoCriptografado = texto.encrypt(Double.toString(dados));
        return dadoCriptografado;
    }

    public String criptografarTimestamp(Long dados) {
        BasicTextEncryptor texto = new BasicTextEncryptor();
        texto.setPasswordCharArray(cifra.toCharArray());
        String dadoCriptografado = texto.encrypt(dados.toString());
        return dadoCriptografado;
    }

    public String descriptografarString(String dados) {
        BasicTextEncryptor texto = new BasicTextEncryptor();
        texto.setPasswordCharArray(cifra.toCharArray());
        String dadoDescriptografado = texto.decrypt(dados);
        return dadoDescriptografado;
    }

    public double descriptografarDouble(String dados) {
        BasicTextEncryptor texto = new BasicTextEncryptor();
        texto.setPasswordCharArray(cifra.toCharArray());
        String dadoDescriptografado = texto.decrypt(dados);
        return Double.parseDouble(dadoDescriptografado);
    }

    public Long descriptografarTimestamp(String dados) {
        BasicTextEncryptor texto = new BasicTextEncryptor();
        texto.setPasswordCharArray(cifra.toCharArray());
        String dadoDescriptografado = texto.decrypt(dados);
        return Long.parseLong(dadoDescriptografado);
    }
}