package com.tms.appconductor.shared.domain.model;

/**
 * 
 * Interfaz para entidades que tienen un identificador único.
 *
 * @param <T> Tipo del identificador (UUID, String, Long, etc.)
 * 
 */

@FunctionalInterface
public interface Identifiable<T> {

    /**
     * 
     * Obtiene el identificador único de la entidad
     * 
     */
    T getId();

    /**
     * 
     * Verifica si la entidad tiene el mismo identificador
     * 
     */
    default boolean hasSameId(T otherId) {
        return getId() != null && getId().equals(otherId);
    }

    /**
     * 
     * Verifica si la entidad tiene un ID asignado
     * 
     */
    default boolean hasId() {
        return getId() != null;
    }
}