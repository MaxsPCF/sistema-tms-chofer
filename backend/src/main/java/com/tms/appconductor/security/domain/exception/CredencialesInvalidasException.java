package com.tms.appconductor.security.domain.exception;

public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException() {
        super("Credenciales inválidas");
    }

    public CredencialesInvalidasException(String message) {
        super(message);
    }
}