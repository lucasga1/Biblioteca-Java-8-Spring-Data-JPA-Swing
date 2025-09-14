package com.alpha7.common.dto;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

/**
 * Classe utilizada para criar o objeto que armazena o status, a mensagem e os erros (objeto ErroCampo) da aplicacao.
 * @author Lucas Gouvea Araujo
 * */
public final class ErroResposta {

    private final int status;
    private final String mensagem;
    private final List<ErroCampo> erros;

    public ErroResposta(int status, String mensagem, List<ErroCampo> erros) {
        this.status = status;
        this.mensagem = mensagem;
        // Garantir que a lista seja imutável
        this.erros = erros != null ? Collections.unmodifiableList(erros) : Collections.emptyList();
    }

    public int getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public List<ErroCampo> getErros() {
        return erros;
    }

    public static ErroResposta respostaPadrao(String mensagem) {
        return new ErroResposta(HttpStatus.BAD_REQUEST.value(), mensagem, Collections.emptyList());
    }

    public static ErroResposta conflito(String mensagem) {
        return new ErroResposta(HttpStatus.CONFLICT.value(), mensagem, Collections.emptyList());
    }
}
