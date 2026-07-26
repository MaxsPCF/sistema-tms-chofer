package com.tms.appconductor.shared.application.wrapper;

import lombok.Getter;

/**
 * 
 * Catálogo de códigos de error estandarizados.
 * 
 */

@Getter

public enum ErrorCode {
    // Errores de autenticación (AUTH-xxx)
    AUTH_INVALID_CREDENTIALS("AUTH-001", "Credenciales inválidas"),
    AUTH_TOKEN_EXPIRED("AUTH-002", "Token expirado"),
    AUTH_TOKEN_INVALID("AUTH-003", "Token inválido"),
    AUTH_ACCOUNT_LOCKED("AUTH-004", "Cuenta bloqueada"),
    AUTH_ACCOUNT_DISABLED("AUTH-005", "Cuenta deshabilitada"),
    AUTH_INSUFFICIENT_PERMISSIONS("AUTH-006", "Permisos insuficientes"),

    // Errores de validación (VAL-xxx)
    VAL_INVALID_INPUT("VAL-001", "Entrada inválida"),
    VAL_REQUIRED_FIELD("VAL-002", "Campo requerido"),
    VAL_INVALID_FORMAT("VAL-003", "Formato inválido"),
    VAL_CONSTRAINT_VIOLATION("VAL-004", "Violación de restricción"),

    // Errores de negocio (BIZ-xxx)
    BIZ_ENTITY_NOT_FOUND("BIZ-001", "Entidad no encontrada"),
    BIZ_DUPLICATE_ENTITY("BIZ-002", "Entidad duplicada"),
    BIZ_BUSINESS_RULE_VIOLATION("BIZ-003", "Violación de regla de negocio"),
    BIZ_OPERATION_NOT_ALLOWED("BIZ-004", "Operación no permitida"),
    BIZ_STATE_TRANSITION_INVALID("BIZ-005", "Transición de estado inválida"),

    // Errores técnicos (TEC-xxx)
    TEC_INTERNAL_ERROR("TEC-001", "Error interno del servidor"),
    TEC_DATABASE_ERROR("TEC-002", "Error de base de datos"),
    TEC_SERVICE_UNAVAILABLE("TEC-003", "Servicio no disponible"),
    TEC_TIMEOUT("TEC-004", "Timeout de operación"),
    TEC_RATE_LIMIT_EXCEEDED("TEC-005", "Límite de tasa excedido");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String format(Object... args) {
        return String.format(defaultMessage, args);
    }
}