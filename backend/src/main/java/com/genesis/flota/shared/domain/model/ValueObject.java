package com.genesis.flota.shared.domain.model;

import java.util.Objects;

/**
 * Clase base para Value Objects en DDD.
 * Los Value Objects son inmutables y se comparan por sus atributos.
 */
public abstract class ValueObject {

    /**
     * Verifica igualdad basada en todos los atributos del Value Object
     */
    @Override
    public abstract boolean equals(Object o);

    /**
     * Genera hashCode basado en todos los atributos
     */
    @Override
    public abstract int hashCode();

    /**
     * Valida que el Value Object sea válido
     */
    protected abstract void validate();

    /**
     * Representación en string del Value Object
     */
    @Override
    public abstract String toString();

    /**
     * Método helper para verificar igualdad de Value Objects
     */
    protected boolean equalHelper(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true; // Las subclases deben implementar la comparación específica
    }
}