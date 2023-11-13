package io.sim;

import java.util.ArrayList;
import org.json.JSONObject;

public class JsonManager {

    private JSONObject obj = new JSONObject();

    public JsonManager() {
    } // Construtor padrão

    public JSONObject JsonTransferencia(String tipoJson, String idPagador, String idRecebedor, String quantia,
            String timestamp) {
        obj.put("tipo_de_requisicao", "CriarConta");
        obj.put("idPagador", idPagador);
        obj.put("idRecebedor", idRecebedor);
        obj.put("quantia", quantia);
        obj.put("timestamp", timestamp);
        // adcionar timestamp
        return obj;
    }

    public JSONObject JsonCriarConta(String idConta, String quantia, String timestamp) {
        obj.put("tipo_de_requisicao", "CriarConta");
        obj.put("idConta", idConta);
        obj.put("quantia", quantia);
        obj.put("timestamp", timestamp);
        return obj;
    }

    public JSONObject JsonSolicitaRota(String idDriver, String timestamp) {
        obj.put("tipo_de_requisicao", "2");
        obj.put("idDriverSolicitante", idDriver);
        obj.put("statusSolicitacao", "nao_atendida");
        obj.put("timestamp", timestamp);
        return obj;
    }
}