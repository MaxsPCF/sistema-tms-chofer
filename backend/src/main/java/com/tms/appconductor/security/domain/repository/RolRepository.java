package com.tms.appconductor.security.domain.repository;

import com.tms.appconductor.security.domain.model.Rol;

import reactor.core.publisher.Flux;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RolRepository {
    Mono<Rol> findById(Integer idRol);

    Mono<Rol> findByNombreRol(String nombreRol);

    Flux<Rol> findByUsuarioId(UUID idUsuario);

    Flux<Rol> findByAplicacionId(Integer idAplicacion);

    Flux<Rol> findActiveRolesByUsuarioId(UUID idUsuario);

    Mono<Boolean> existsByNombreRol(String nombreRol);

    Mono<Rol> save(Rol rol);

    Mono<Void> deleteById(Integer idRol);
}