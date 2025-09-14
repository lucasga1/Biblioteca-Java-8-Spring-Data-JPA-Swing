package com.alpha7.common.exception;

/**
 * Excessao personalizada do sistema.
 * @author Lucas Gouvea Araujo
 * */
public class DeletarLivroException extends RuntimeException {
    public DeletarLivroException(String message) {
        super(message);
    }
}
