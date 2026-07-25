package com.genesis.flota.shared.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Atributos de error estandarizados para todas las respuestas de error.
 * Siguiendo el estándar RFC 7807 (Problem Details for HTTP APIs).
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorAttributes {

    /**
     * URI que identifica el tipo de error
     */
    private String type;

    /**
     * Título corto del error
     */
    private String title;

    /**
     * Código HTTP del error
     */
    private int status;

    /**
     * Detalle descriptivo del error
     */
    private String detail;

    /**
     * URI de la instancia que causó el error
     */
    private String instance;

    /**
     * Timestamp de cuando ocurrió el error
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Código de error de la aplicación
     */
    private String errorCode;

    /**
     * ID único de trazabilidad del error
     */
    private String traceId;

    /**
     * Lista de errores de validación (si aplica)
     */
    private List<ValidationError> validationErrors;

    /**
     * Metadatos adicionales del error
     */
    private Map<String, Object> metadata;

    /**
     * Error de validación individual
     */
    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ValidationError {
        private String field;
        private String message;
        private Object rejectedValue;
        private String code;

        public static ValidationError of(String field, String message) {
            return ValidationError.builder()
                    .field(field)
                    .message(message)
                    .build();
        }

        public static ValidationError of(String field, String message, Object rejectedValue) {
            return ValidationError.builder()
                    .field(field)
                    .message(message)
                    .rejectedValue(rejectedValue)
                    .build();
        }

        public static ValidationError of(String field, String message, Object rejectedValue, String code) {
            return ValidationError.builder()
                    .field(field)
                    .message(message)
                    .rejectedValue(rejectedValue)
                    .code(code)
                    .build();
        }
    }

    /**
     * Crea atributos de error para errores de validación
     */
    public static ErrorAttributes forValidation(
            List<ValidationError> errors,
            String instance,
            String traceId) {
        return ErrorAttributes.builder()
                .type("https://api.genesis.com/errors/validation-error")
                .title("Error de Validación")
                .status(400)
                .detail("La solicitud contiene datos inválidos")
                .instance(instance)
                .errorCode("VAL-001")
                .traceId(traceId)
                .validationErrors(errors)
                .build();
    }

    /**
     * Crea atributos de error para entidad no encontrada
     */
    public static ErrorAttributes forNotFound(
            String entityName,
            Object entityId,
            String instance,
            String traceId) {
        return ErrorAttributes.builder()
                .type("https://api.genesis.com/errors/not-found")
                .title("Recurso No Encontrado")
                .status(404)
                .detail(String.format("%s no encontrado con ID: %s", entityName, entityId))
                .instance(instance)
                .errorCode("BIZ-001")
                .traceId(traceId)
                .metadata(Map.of(
                        "entityName", entityName,
                        "entityId", entityId
                ))
                .build();
    }

    /**
     * Crea atributos de error para errores de autenticación
     */
    public static ErrorAttributes forAuthentication(
            String detail,
            String instance,
            String traceId) {
        return ErrorAttributes.builder()
                .type("https://api.genesis.com/errors/authentication-error")
                .title("Error de Autenticación")
                .status(401)
                .detail(detail)
                .instance(instance)
                .errorCode("AUTH-001")
                .traceId(traceId)
                .build();
    }

    /**
     * Crea atributos de error para acceso denegado
     */
    public static ErrorAttributes forAccessDenied(
            String detail,
            String instance,
            String traceId) {
        return ErrorAttributes.builder()
                .type("https://api.genesis.com/errors/forbidden")
                .title("Acceso Denegado")
                .status(403)
                .detail(detail)
                .instance(instance)
                .errorCode("AUTH-006")
                .traceId(traceId)
                .build();
    }

    /**
     * Crea atributos de error para errores de negocio
     */
    public static ErrorAttributes forBusinessError(
            String detail,
            String errorCode,
            String instance,
            String traceId) {
        return ErrorAttributes.builder()
                .type("https://api.genesis.com/errors/business-error")
                .title("Error de Negocio")
                .status(422)
                .detail(detail)
                .instance(instance)
                .errorCode(errorCode)
                .traceId(traceId)
                .build();
    }

    /**
     * Crea atributos de error para cuenta bloqueada
     */
    public static ErrorAttributes forLocked(
            String detail,
            String instance,
            String traceId) {
        return ErrorAttributes.builder()
                .type("https://api.genesis.com/errors/account-locked")
                .title("Cuenta Bloqueada")
                .status(423)
                .detail(detail)
                .instance(instance)
                .errorCode("AUTH-004")
                .traceId(traceId)
                .build();
    }

    /**
     * Crea atributos de error para rate limiting
     */
    public static ErrorAttributes forRateLimit(
            String detail,
            String instance,
            String traceId) {
        return ErrorAttributes.builder()
                .type("https://api.genesis.com/errors/rate-limit-exceeded")
                .title("Límite de Solicitudes Excedido")
                .status(429)
                .detail(detail)
                .instance(instance)
                .errorCode("TEC-005")
                .traceId(traceId)
                .build();
    }

    /**
     * Crea atributos de error para errores internos del servidor
     */
    public static ErrorAttributes forInternalError(
            String detail,
            String instance,
            String traceId) {
        return ErrorAttributes.builder()
                .type("https://api.genesis.com/errors/internal-error")
                .title("Error Interno del Servidor")
                .status(500)
                .detail("Se ha producido un error interno. Por favor, contacte al administrador.")
                .instance(instance)
                .errorCode("TEC-001")
                .traceId(traceId)
                .metadata(Map.of("originalError", detail))
                .build();
    }

    /**
     * Crea atributos de error para servicio no disponible
     */
    public static ErrorAttributes forServiceUnavailable(
            String detail,
            String instance,
            String traceId) {
        return ErrorAttributes.builder()
                .type("https://api.genesis.com/errors/service-unavailable")
                .title("Servicio No Disponible")
                .status(503)
                .detail(detail)
                .instance(instance)
                .errorCode("TEC-003")
                .traceId(traceId)
                .build();
    }

    /**
     * Convierte a un mapa para respuestas simples
     */
    public Map<String, Object> toMap() {
        return Map.of(
                "type", type != null ? type : "",
                "title", title != null ? title : "",
                "status", status,
                "detail", detail != null ? detail : "",
                "instance", instance != null ? instance : "",
                "timestamp", timestamp != null ? timestamp.toString() : "",
                "errorCode", errorCode != null ? errorCode : "",
                "traceId", traceId != null ? traceId : ""
        );
    }
}