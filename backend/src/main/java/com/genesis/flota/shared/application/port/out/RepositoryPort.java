package com.genesis.flota.shared.application.port.out;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Puerto genérico de repositorio (Output Port).
 * Define operaciones CRUD estándar para cualquier entidad.
 *
 * @param <T> Tipo de la entidad
 * @param <ID> Tipo del identificador
 */
public interface RepositoryPort<T, ID> {

    Mono<T> findById(ID id);

    Flux<T> findAll();

    Mono<T> save(T entity);

    Mono<Void> deleteById(ID id);

    Mono<Boolean> existsById(ID id);

    Mono<Long> count();
}