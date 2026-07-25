package com.genesis.flota.shared.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clase base para eventos de dominio.
 * Los eventos de dominio representan algo que ocurrió en el dominio.
 */
@Getter
public abstract class DomainEvent {

    private final UUID eventId;
    private final LocalDateTime occurredOn;
    private final String eventType;
    private final UUID aggregateId;

    protected DomainEvent(UUID aggregateId) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
        this.eventType = this.getClass().getSimpleName();
        this.aggregateId = aggregateId;
    }

    /**
     * Nombre del evento para serialización/deserialización
     */
    public String getEventName() {
        return eventType;
    }

    /**
     * Timestamp de cuando ocurrió el evento
     */
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    /**
     * ID del agregado que generó el evento
     */
    public UUID getAggregateId() {
        return aggregateId;
    }

    @Override
    public String toString() {
        return String.format("%s[eventId=%s, aggregateId=%s, occurredOn=%s]",
                eventType, eventId, aggregateId, occurredOn);
    }
}