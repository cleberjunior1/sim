package io.sim;

import com.google.gson.Gson;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import java.security.Key;

public class JSONCryptoUtil {
    private SecretKey secretKey;
    private Gson gson;

    public JSONCryptoUtil() {
        // Inicialize a chave de criptografia (pode ser mais seguro armazená-la em um local seguro)
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            secretKey = keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        gson = new Gson();
    }

    public String encryptToJSON(Object data) {
        try {
            // Serializa o objeto Java em JSON
            String jsonData = gson.toJson(data);

            // Criptografa os dados
            Cipher cipher = Cipher.getInstance("AES");
            Key key = secretKey;
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(jsonData.getBytes());

            // Retorna os dados criptografados em formato JSON
            return new String(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T decryptFromJSON(String encryptedJSON, Class<T> dataType) {
        try {
            // Descriptografa os dados
            Cipher cipher = Cipher.getInstance("AES");
            Key key = secretKey;
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedData = cipher.doFinal(encryptedJSON.getBytes());

            // Desserializa o JSON em um objeto Java
            String decryptedJSON = new String(decryptedData);
            return gson.fromJson(decryptedJSON, dataType);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

