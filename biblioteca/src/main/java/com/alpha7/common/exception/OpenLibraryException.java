package com.alpha7.common.exception;

/**
 * Excessao personalizada do sistema.
 * @author Lucas Gouvea Araujo
 * */
public class OpenLibraryException extends RuntimeException {
    public OpenLibraryException(String message) {
        super(message);
    }
}
