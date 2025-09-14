package com.alpha7.common.exception;

/**
 * Excessao personalizada do sistema.
 * @author Lucas Gouvea Araujo
 * */
public class ConversaoInputsException extends RuntimeException {
    public ConversaoInputsException(String message) {
        super(message);
    }
}
