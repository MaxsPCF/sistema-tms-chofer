package com.genesis.flota.security.domain.repository;

import com.genesis.flota.security.domain.model.Usuario;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UsuarioRepository {
    Mono<Usuario> findById(UUID id);
    Mono<Usuario> findByNombreUsuario(String nombreUsuario);
    Mono<Usuario> findByEmail(String email);
    /**
     * Buscar usuario por email CON sus roles y aplicación cargados
     */
    Mono<Usuario> findByEmailWithRoles(String email);
    Mono<Boolean> existsByNombreUsuario(String nombreUsuario);
    Mono<Boolean> existsByEmail(String email);
    Mono<Usuario> save(Usuario usuario);
    Mono<Void> updateLoginAttempts(UUID id, int attempts);
    Mono<Void> updateBlockedStatus(UUID id, boolean blocked);
}