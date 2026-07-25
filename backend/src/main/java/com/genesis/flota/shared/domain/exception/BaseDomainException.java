package com.genesis.flota.shared.domain.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Excepción base para todas las excepciones de dominio.
 */
@Getter
public abstract class BaseDomainException extends RuntimeException {

    private final String errorCode;
    private final String errorId;
    private final LocalDateTime timestamp;

    protected BaseDomainException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.errorId = UUID.randomUUID().toString().substring(0, 8);
        this.timestamp = LocalDateTime.now();
    }

    protected BaseDomainException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorId = UUID.randomUUID().toString().substring(0, 8);
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Código de error único para identificar el tipo de excepción
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * ID único de la instancia de error (para trazabilidad)
     */
    public String getErrorId() {
        return errorId;
    }

    /**
     * Detalles adicionales del error
     */
    public abstract String getErrorDetail();

    @Override
    public String toString() {
        return String.format("[%s][%s] %s", errorCode, errorId, getMessage());
    }
}