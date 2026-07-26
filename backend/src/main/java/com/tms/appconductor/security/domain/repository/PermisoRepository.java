package com.tms.appconductor.security.domain.repository;

import com.tms.appconductor.security.domain.model.Permiso;

import reactor.core.publisher.Flux;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PermisoRepository {
    Flux<Permiso> findByUsuarioId(UUID idUsuario);

    Flux<Permiso> findByRolId(Integer idRol);

    Mono<Permiso> findByRolIdAndModuloId(Integer idRol, Integer idModulo);
}