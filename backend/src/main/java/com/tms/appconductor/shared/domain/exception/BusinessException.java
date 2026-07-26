package com.tms.appconductor.shared.domain.exception;

/**
 * 
 * Excepción para violaciones de reglas de negocio.
 * 
 * Debe usarse cuando una operación viola una regla del dominio.
 * 
 */
public class BusinessException extends BaseDomainException {
    public BusinessException(String message, String errorCode) {
        super(message, errorCode);
    }

    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }

    public static BusinessException of(String message) {
        return new BusinessException(message, "BUSINESS_RULE_VIOLATION");
    }

    public static BusinessException of(String message, String errorCode) {
        return new BusinessException(message, errorCode);
    }

    @Override
    public String getErrorDetail() {
        return "Violación de regla de negocio: " + getMessage();
    }
}