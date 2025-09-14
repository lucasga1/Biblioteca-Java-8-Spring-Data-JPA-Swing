package com.alpha7.common.exception;

/**
 * Excessao personalizada do sistema.
 * @author Lucas Gouvea Araujo
 * */
public class BuscarLivroException extends RuntimeException {
    public BuscarLivroException(String message) {
        super(message);
    }
}
