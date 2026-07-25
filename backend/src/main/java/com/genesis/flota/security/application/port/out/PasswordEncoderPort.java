package com.genesis.flota.security.application.port.out;

import reactor.core.publisher.Mono;

/**
 * Puerto de salida para codificación de contraseñas.
 * Permite abstraer la implementación de hashing (BCrypt, PBKDF2, etc.)
 */
public interface PasswordEncoderPort {

    /**
     * Codifica una contraseña en texto plano
     */
    Mono<String> encodeReactive(String rawPassword);

    /**
     * Verifica si una contraseña en texto plano coincide con un hash
     */
    Mono<Boolean> matchesReactive(String rawPassword, String encodedPassword);

    /**
     * Verifica si un hash necesita ser actualizado (ej: cambió el work factor)
     */
    Mono<Boolean> needsRehash(String encodedPassword);
}