package io.sim;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Semaphore;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

public class SharedMemory {
    private JSONObject data;
    private Semaphore semaphore;

    public SharedMemory() {
        this.data = null;
        this.semaphore = new Semaphore(1);
    }

    public void write(JSONObject value, String nomeArquivo) {
        try {
            semaphore.acquire();
            // this.data = value;
            saveFile(value, nomeArquivo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

    public JSONArray read() { // retorna o json
        JSONArray jsonArray = new JSONArray();
        String filePath = "src\\main\\java\\io\\sim\\jsons\\";

        try (Stream<Path> paths = Files.walk(Paths.get(filePath))) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .forEach(file -> {
                        try {
                            String content = new String(Files.readAllBytes(file));
                            JSONObject json = new JSONObject(content);
                            jsonArray.put(json);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    private void saveFile(JSONObject value, String nomeArquivo) { // salva o arquivo
        try (FileWriter file = new FileWriter("src\\main\\java\\io\\sim\\jsons\\" + nomeArquivo + ".json")) {
            file.write(value.toString());
            file.flush(); // limpa o buffer

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getFile(String requisicao) {
        JSONObject json = new JSONObject("chave", "Erro ao gerar json");
        try {
            String path = "/jsons/" + requisicao + ".json"; // Substitua pelo caminho do seu arquivo
            String content = new String(Files.readAllBytes(Paths.get(path)));
            json = new JSONObject(content);
            return json;
            // System.out.println(json.toString(4)); // Imprime o JSON formatado com 4
            // espaços de indentação
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}