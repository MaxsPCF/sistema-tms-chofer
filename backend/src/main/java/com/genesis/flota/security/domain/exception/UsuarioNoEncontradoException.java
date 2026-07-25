package com.genesis.flota.security.domain.exception;

import java.util.UUID;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(String username) {
        super("Usuario no encontrado: " + username);
    }

    public UsuarioNoEncontradoException(UUID id) {
        super("Usuario no encontrado con ID: " + id);
    }
}