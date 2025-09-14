package com.alpha7.common.exception;

/**
 * Excessao personalizada do sistema.
 * @author Lucas Gouvea Araujo
 * */
public class CadastroLivroViaCSVException extends RuntimeException {
    public CadastroLivroViaCSVException(String message) {
        super(message);
    }
}
