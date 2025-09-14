package com.alpha7.common.exception;

/**
 * Excessao personalizada do sistema.
 * @author Lucas Gouvea Araujo
 * */
public class AtualizarLivroException extends RuntimeException {
    public AtualizarLivroException(String message) {
        super(message);
    }
}
