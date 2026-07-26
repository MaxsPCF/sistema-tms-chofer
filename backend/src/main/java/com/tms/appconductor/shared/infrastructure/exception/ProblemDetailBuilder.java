package com.tms.appconductor.shared.infrastructure.exception;

import com.tms.appconductor.shared.domain.exception.BaseDomainException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * 
 * Builder para crear Problem Details siguiendo el estándar RFC 7807.
 * 
 * Problem Details for HTTP APIs.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7807">RFC 7807</a>
 * 
 */

@Slf4j
public class ProblemDetailBuilder {
    private String type;
    private String title;
    private int status;
    private String detail;
    private String instance;
    private Instant timestamp;
    private String errorCode;
    private String traceId;
    private List<ErrorAttributes.ValidationError> errors;
    private Throwable cause;

    private ProblemDetailBuilder() {
        this.timestamp = Instant.now();
        this.traceId = UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 
     * Crea una nueva instancia del builder
     * 
     */
    public static ProblemDetailBuilder create() {
        return new ProblemDetailBuilder();
    }

    /**
     * 
     * Crea un builder desde una excepción
     * 
     */
    public static ProblemDetailBuilder fromException(Throwable ex) {
        ProblemDetailBuilder builder = create();
        builder.cause = ex;
        builder.detail = ex.getMessage();

        if (ex instanceof BaseDomainException domainEx) {
            builder.errorCode = domainEx.getErrorCode();
            builder.traceId = domainEx.getErrorId();
        }

        return builder;
    }

    /**
     * 
     * Crea un builder desde un intercambio web
     * 
     */
    public static ProblemDetailBuilder fromExchange(ServerWebExchange exchange) {
        ProblemDetailBuilder builder = create();
        builder.instance = exchange.getRequest().getPath().value();
        // Extraer trace ID del header si existe
        String traceHeader = exchange.getRequest().getHeaders()
                .getFirst("X-Trace-ID");

        if (traceHeader != null) {
            builder.traceId = traceHeader;
        }

        return builder;
    }

    /**
     * 
     * Establece el tipo de problema (URI)
     * 
     */
    public ProblemDetailBuilder withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * 
     * Establece el título del error
     * 
     */
    public ProblemDetailBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 
     * Establece el código HTTP
     * 
     */
    public ProblemDetailBuilder withStatus(HttpStatus status) {
        this.status = status.value();
        return this;
    }

    /**
     * 
     * Establece el código HTTP numérico
     * 
     */
    public ProblemDetailBuilder withStatus(int status) {
        this.status = status;
        return this;
    }

    /**
     * 
     * Establece el detalle del error
     * 
     */
    public ProblemDetailBuilder withDetail(String detail) {
        this.detail = detail;
        return this;
    }

    /**
     * 
     * Establece la instancia (URI del recurso)
     * 
     */
    public ProblemDetailBuilder withInstance(String instance) {
        this.instance = instance;
        return this;
    }

    /**
     * 
     * Establece el código de error de la aplicación
     * 
     */
    public ProblemDetailBuilder withErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    /**
     * 
     * Establece el ID de trazabilidad
     * 
     */
    public ProblemDetailBuilder withTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    /**
     * 
     * Establece errores de validación
     * 
     */
    public ProblemDetailBuilder withValidationErrors(List<ErrorAttributes.ValidationError> errors) {
        this.errors = errors;
        return this;
    }

    /**
     * 
     * Agrega un error de validación
     * 
     */
    public ProblemDetailBuilder withValidationError(String field, String message) {
        if (this.errors == null) {
            this.errors = new java.util.ArrayList<>();
        }

        this.errors.add(ErrorAttributes.ValidationError.of(field, message));
        return this;
    }

    /**
     * 
     * Agrega un error de validación con valor rechazado
     * 
     */
    public ProblemDetailBuilder withValidationError(String field, String message, Object rejectedValue) {
        if (this.errors == null) {
            this.errors = new java.util.ArrayList<>();
        }

        this.errors.add(ErrorAttributes.ValidationError.of(field, message, rejectedValue));
        return this;
    }

    /**
     * 
     * Construye para error de validación (400)
     * 
     */
    public ProblemDetailBuilder forValidationError() {
        return this
                .withType("https://api.tms-transporte.com/errors/validation-error")
                .withTitle("Error de Validación")
                .withStatus(HttpStatus.BAD_REQUEST)
                .withErrorCode("VAL-001");
    }

    /**
     * 
     * Construye para entidad no encontrada (404)
     * 
     */
    public ProblemDetailBuilder forNotFound(String entityName, Object entityId) {
        return this
                .withType("https://api.tms-transporte.com/errors/not-found")
                .withTitle("Recurso No Encontrado")
                .withStatus(HttpStatus.NOT_FOUND)
                .withErrorCode("BIZ-001")
                .withDetail(String.format("%s no encontrado con ID: %s", entityName, entityId));
    }

    /**
     * 
     * Construye para error de autenticación (401)
     * 
     */
    public ProblemDetailBuilder forAuthenticationError() {
        return this
                .withType("https://api.tms-transporte.com/errors/authentication-error")
                .withTitle("Error de Autenticación")
                .withStatus(HttpStatus.UNAUTHORIZED)
                .withErrorCode("AUTH-001");
    }

    /**
     * 
     * Construye para acceso denegado (403)
     * 
     */
    public ProblemDetailBuilder forAccessDenied() {
        return this
                .withType("https://api.tms-transporte.com/errors/forbidden")
                .withTitle("Acceso Denegado")
                .withStatus(HttpStatus.FORBIDDEN)
                .withErrorCode("AUTH-006");
    }

    /**
     * 
     * Construye para cuenta bloqueada (423)
     * 
     */
    public ProblemDetailBuilder forAccountLocked() {
        return this
                .withType("https://api.tms-transporte.com/errors/account-locked")
                .withTitle("Cuenta Bloqueada")
                .withStatus(423) // HttpStatus.LOCKED
                .withErrorCode("AUTH-004");
    }

    /**
     * 
     * Construye para error de negocio (422)
     * 
     */
    public ProblemDetailBuilder forBusinessError() {
        return this
                .withType("https://api.tms-transporte.com/errors/business-error")
                .withTitle("Error de Negocio")
                .withStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                .withErrorCode("BIZ-003");
    }

    /**
     * 
     * Construye para rate limit (429)
     * 
     */
    public ProblemDetailBuilder forRateLimit() {
        return this
                .withType("https://api.tms-transporte.com/errors/rate-limit-exceeded")
                .withTitle("Límite de Solicitudes Excedido")
                .withStatus(429) // HttpStatus.TOO_MANY_REQUESTS
                .withErrorCode("TEC-005");
    }

    /**
     * 
     * Construye para error interno (500)
     * 
     */
    public ProblemDetailBuilder forInternalError() {
        return this
                .withType("https://api.tms-transporte.com/errors/internal-error")
                .withTitle("Error Interno del Servidor")
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .withErrorCode("TEC-001")
                .withDetail("Se ha producido un error interno. Por favor, contacte al administrador.");
    }

    /**
     * 
     * Construye para servicio no disponible (503)
     * 
     */
    public ProblemDetailBuilder forServiceUnavailable() {
        return this
                .withType("https://api.tms-transporte.com/errors/service-unavailable")
                .withTitle("Servicio No Disponible")
                .withStatus(HttpStatus.SERVICE_UNAVAILABLE)
                .withErrorCode("TEC-003");
    }

    /**
     * 
     * Construye los ErrorAttributes finales
     * 
     */
    public ErrorAttributes build() {
        return ErrorAttributes.builder()
                .type(type)
                .title(title != null ? title : HttpStatus.valueOf(status).getReasonPhrase())
                .status(status)
                .detail(detail)
                .instance(instance)
                .timestamp(timestamp != null ? java.time.LocalDateTime.ofInstant(timestamp, java.time.ZoneId.of("UTC"))
                        : null)
                .errorCode(errorCode)
                .traceId(traceId)
                .validationErrors(errors)
                .build();
    }

    /**
     * 
     * Construye y loguea el error
     * 
     */
    public ErrorAttributes buildAndLog() {
        ErrorAttributes error = build();
        if (status >= 500) {
            log.error("Error {} - {}: {} [traceId: {}]",
                    status, errorCode, detail, traceId, cause);
        } else if (status >= 400) {
            log.warn("Error {} - {}: {} [traceId: {}]",
                    status, errorCode, detail, traceId);
        }

        return error;
    }

    /**
     * 
     * Obtiene el traceId generado
     * 
     */
    public String getTraceId() {
        return traceId;
    }

    /**
     * 
     * Obtiene el código de error
     * 
     */
    public String getErrorCode() {
        return errorCode;
    }
}