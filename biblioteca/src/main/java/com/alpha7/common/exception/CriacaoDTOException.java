package com.alpha7.common.exception;

/**
 * Excessao personalizada do sistema.
 * @author Lucas Gouvea Araujo
 * */
public class CriacaoDTOException extends RuntimeException {
    public CriacaoDTOException(String message) {
        super(message);
    }
}
