package com.genesis.flota.shared.domain.service;

/**
 * Interfaz marcadora para Servicios de Dominio.
 * Los servicios de dominio contienen lógica de negocio que no pertenece
 * naturalmente a una entidad o value object.
 */
public interface DomainService {

    /**
     * Nombre del servicio para logging y monitoreo
     */
    default String getServiceName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Descripción de la responsabilidad del servicio
     */
    default String getServiceDescription() {
        return "Servicio de dominio: " + getServiceName();
    }
}