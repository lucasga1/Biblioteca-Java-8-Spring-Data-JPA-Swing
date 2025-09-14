package com.alpha7.common.exception;

/**
 * Excessao personalizada do sistema.
 * @author Lucas Gouvea Araujo
 * */
public class ParametrosBuscaException extends RuntimeException {
    public ParametrosBuscaException(String message) {
        super(message);
    }
}
