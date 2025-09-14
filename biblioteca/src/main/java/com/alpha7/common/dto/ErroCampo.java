package com.alpha7.common.dto;

/**
 * Classe utilizada para criar o objeto que armazena os erros da aplicacao.
 * @author Lucas Gouvea Araujo
 * */
public class ErroCampo {

    private String campo;
    private String erro;

    public ErroCampo() {
    }

    public ErroCampo(String campo, String erro) {
        this.campo = campo;
        this.erro = erro;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getErro() {
        return erro;
    }
}
