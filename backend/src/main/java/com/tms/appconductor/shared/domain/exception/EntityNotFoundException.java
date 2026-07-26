package com.tms.appconductor.shared.domain.exception;

import java.util.UUID;

/**
 * 
 * Excepción lanzada cuando una entidad no se encuentra.
 * 
 */

public class EntityNotFoundException extends BaseDomainException {
    private final String entityName;
    private final Object entityId;

    public EntityNotFoundException(String entityName, Object entityId) {
        super(String.format("%s no encontrado con ID: %s", entityName, entityId),
                "ENTITY_NOT_FOUND");

        this.entityName = entityName;
        this.entityId = entityId;
    }

    public EntityNotFoundException(String entityName, UUID entityId) {
        this(entityName, (Object) entityId);
    }

    public EntityNotFoundException(String entityName, String entityId) {
        this(entityName, (Object) entityId);
    }

    public String getEntityName() {
        return entityName;
    }

    public Object getEntityId() {
        return entityId;
    }

    @Override
    public String getErrorDetail() {
        return String.format("Entidad '%s' con ID '%s' no encontrada",
                entityName, entityId);
    }

    public static EntityNotFoundException of(String entityName, Object id) {
        return new EntityNotFoundException(entityName, id);
    }
}