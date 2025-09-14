package com.alpha7.common.dto;

/**
 * Classe responsavel por pegar os retornos dos endpoints.
 * Os dados retornados ou erros retornados.
 * @author Lucas Gouvea Araujo
 * */
public class Resposta<T> {
    private T data;
    private String erro;

    public Resposta() {}
    public Resposta(T data) {
        this.data = data;
    }
    public Resposta(String erro) {
        this.erro = erro;
    }

    public T getData() { return data; }
    public String getErro() { return erro; }
    public boolean isOk() { return data != null; }
}