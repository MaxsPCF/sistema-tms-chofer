package com.genesis.flota.shared.domain.exception;

import java.util.List;

/**
 * Excepción para errores de validación.
 * Puede contener múltiples mensajes de error.
 */
public class ValidationException extends BaseDomainException {

    private final List<String> validationErrors;

    public ValidationException(String message, List<String> validationErrors) {
        super(message, "VALIDATION_ERROR");
        this.validationErrors = validationErrors;
    }

    public ValidationException(String message) {
        this(message, List.of(message));
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public static ValidationException withErrors(List<String> errors) {
        return new ValidationException("Error de validación", errors);
    }

    @Override
    public String getErrorDetail() {
        return String.format("Errores de validación: %s",
                String.join(", ", validationErrors));
    }
}