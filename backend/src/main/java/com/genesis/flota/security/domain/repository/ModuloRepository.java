package com.genesis.flota.security.domain.repository;

import com.genesis.flota.security.domain.model.Modulo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ModuloRepository {
    Flux<Modulo> findMenusByUsuarioId(UUID idUsuario);
    Flux<Modulo> findMenusByUsuarioIdAndAplicacionId(UUID idUsuario, Integer idAplicacion);
    /**
     * Buscar menús por código de aplicación (APP_CONDUCTOR, etc.)
     */
    Flux<Modulo> findMenusByUsuarioIdAndAplicacionCodigo(UUID idUsuario, String codigoAplicacion);
    Flux<Modulo> findByAplicacionId(Integer idAplicacion);
    Mono<Modulo> findById(Integer idModulo);
}