package com.genesis.flota.shared.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Clase base para todas las entidades del dominio.
 * Proporciona comportamiento común como ID, auditoría y eventos de dominio.
 */
@Getter
public abstract class BaseEntity implements Identifiable<UUID> {

    protected UUID id;
    protected LocalDateTime fechaCreacion;
    protected UUID usuarioCreacion;
    protected LocalDateTime fechaModificacion;
    protected UUID usuarioModifica;

    private List<DomainEvent> domainEvents = new ArrayList<>();

    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.fechaCreacion = LocalDateTime.now();
    }

    protected BaseEntity(UUID id) {
        this.id = id;
        this.fechaCreacion = LocalDateTime.now();
    }

    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Registra un evento de dominio para ser publicado después
     */
    protected void registerEvent(DomainEvent event) {
        if (domainEvents == null) {
            domainEvents = new ArrayList<>();
        }
        domainEvents.add(event);
    }

    /**
     * Obtiene los eventos de dominio pendientes de publicar
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Limpia los eventos de dominio después de publicarlos
     */
    public void clearDomainEvents() {
        domainEvents.clear();
    }

    /**
     * Marca la entidad como modificada
     */
    public void markAsModified(UUID usuarioModifica) {
        this.fechaModificacion = LocalDateTime.now();
        this.usuarioModifica = usuarioModifica;
    }

    /**
     * Marca la entidad como creada
     */
    public void markAsCreated(UUID usuarioCreacion) {
        this.fechaCreacion = LocalDateTime.now();
        this.usuarioCreacion = usuarioCreacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + '}';
    }
}