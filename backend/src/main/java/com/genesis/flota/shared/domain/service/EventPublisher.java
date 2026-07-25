package com.genesis.flota.shared.domain.service;

import com.genesis.flota.shared.domain.model.DomainEvent;
import reactor.core.publisher.Mono;

/**
 * Servicio de dominio para publicación de eventos.
 * Separa la publicación de eventos de la lógica de negocio.
 */
public interface EventPublisher extends DomainService {

    /**
     * Publica un evento de dominio
     */
    Mono<Void> publish(DomainEvent event);

    /**
     * Publica múltiples eventos de dominio
     */
    Mono<Void> publishAll(Iterable<DomainEvent> events);

    /**
     * Verifica si hay suscriptores para un tipo de evento
     */
    Mono<Boolean> hasSubscribersFor(Class<? extends DomainEvent> eventType);
}