package com.tms.appconductor.shared.application.port.out;

import com.tms.appconductor.shared.domain.model.DomainEvent;

import reactor.core.publisher.Mono;

/**
 * 
 * Puerto de salida para el bus de eventos.
 * 
 * Permite publicar eventos de dominio a sistemas externos.
 * 
 */
public interface EventBusPort {

    /**
     * 
     * Publica un evento al bus
     * 
     */

    Mono<Void> publish(DomainEvent event);

    /**
     * 
     * Publica un evento con metadatos adicionales
     * 
     */

    Mono<Void> publish(DomainEvent event, String routingKey);

    /**
     * 
     * Publica múltiples eventos en lote
     * 
     */

    Mono<Void> publishBatch(Iterable<DomainEvent> events);

}