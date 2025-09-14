package com.alpha7.view.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe utilitária para capturar a mensagem de erro de uma string JSON ou texto puro.
 *
 * Esta classe analisa a string de erro, identifica se existe um JSON contido na mensagem
 * e extrai o valor do campo "mensagem" caso ele exista. Se não houver JSON ou se ocorrer
 * algum problema na leitura, a própria string de erro é retornada.
 *
 * Uso:
 * - Captura a mensagem principal de erro de respostas de APIs ou exceções.
 *
 * Exemplo:
 * CapturarMensagemErro util = new CapturarMensagemErro();
 * String msg = util.capturarMensagemErro(erro);
 *
 * @author Lucas Gouvea Araujo
 * @version 1.0
 */
public class CapturarMensagemErro {

    public String capturarMensagemErro(String erro) throws JsonProcessingException {
        String mensagemErro = null;
        try {
            int indiceJson = erro.indexOf("{"); // Inicia a contagem do indice a partir da {
            if (indiceJson != -1) { // Percorre o objeto, se nao encontrar nada, retorna -1
                String json = erro.substring(indiceJson);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(json);
                if (node.has("mensagem")) {
                    mensagemErro = node.get("mensagem").asText();
                }
            } else {
                mensagemErro = erro;
            }
        } catch (Exception e) {
            mensagemErro = erro;
        }
        return mensagemErro;
    }
}
