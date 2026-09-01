package com.acme.sica.shared.domain.exception;

public class EstadoIlegalException extends RuntimeException {
    public EstadoIlegalException(String message) {
        super(message);
    }
}
