package com.genesis.flota.security.domain.exception;

public class CuentaBloqueadaException extends RuntimeException {
    public CuentaBloqueadaException(String username) {
        super("La cuenta del usuario '" + username + "' está bloqueada");
    }
}