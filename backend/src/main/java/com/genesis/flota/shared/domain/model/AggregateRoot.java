package com.genesis.flota.shared.domain.model;

import java.util.UUID;

/**
 * Clase base para Aggregate Roots en DDD.
 * Un Aggregate Root es la entidad principal que garantiza
 * la consistencia de un agregado.
 */
public abstract class AggregateRoot extends BaseEntity {

    private long version;

    protected AggregateRoot() {
        super();
        this.version = 1L;
    }

    protected AggregateRoot(UUID id) {
        super(id);
        this.version = 1L;
    }

    /**
     * Incrementa la versión del agregado (optimistic locking)
     */
    protected void incrementVersion() {
        this.version++;
    }

    /**
     * Obtiene la versión actual del agregado
     */
    public long getVersion() {
        return version;
    }

    /**
     * Verifica si el agregado es nuevo (no persistido)
     */
    public boolean isNew() {
        return version == 1L;
    }

    /**
     * Valida las reglas de negocio del agregado
     */
    protected abstract void validate();
}