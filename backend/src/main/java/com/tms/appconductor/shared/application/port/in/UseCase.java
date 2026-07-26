package com.tms.appconductor.shared.application.port.in;

import reactor.core.publisher.Mono;

/**
 * 
 * Interfaz base para casos de uso (Input Ports).
 *
 * @param <C> Tipo del comando/query de entrada
 * 
 * @param <R> Tipo del resultado
 * 
 */

@FunctionalInterface

public interface UseCase<C, R> {
    /**
     * 
     * Ejecuta el caso de uso
     * 
     */

    Mono<R> execute(C command);

    /**
     * 
     * Nombre del caso de uso para logging
     * 
     */

    default String getUseCaseName() {
        return this.getClass().getSimpleName();
    }
}