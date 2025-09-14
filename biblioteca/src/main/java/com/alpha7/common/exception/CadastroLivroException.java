package com.alpha7.common.exception;

/**
 * Excessao personalizada do sistema.
 * @author Lucas Gouvea Araujo
 * */
public class CadastroLivroException extends RuntimeException {
    public CadastroLivroException(String message) {
        super(message);
    }
}
