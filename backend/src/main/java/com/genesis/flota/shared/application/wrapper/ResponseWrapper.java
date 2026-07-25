package com.genesis.flota.shared.application.wrapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Envoltorio universal de respuestas de la API")
public record ResponseWrapper<T>(

        @Schema(description = "Indica si la operación fue exitosa", example = "true")
        boolean success,

        @Schema(description = "Datos de la respuesta")
        T data,

        @Schema(description = "Mensaje descriptivo de la operación", example = "Operación exitosa")
        String message,

        @Schema(description = "Lista de errores si la operación falló")
        List<String> errors,

        @Schema(description = "Timestamp de la respuesta")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime timestamp,

        @Schema(description = "ID de trazabilidad de la petición")
        String traceId
) {
    // Constructor canónico con valores por defecto
    public ResponseWrapper {
        if (errors == null) errors = Collections.emptyList();
        if (timestamp == null) timestamp = LocalDateTime.now();
        if (traceId == null) traceId = UUID.randomUUID().toString().substring(0, 8);
    }

    // Factory methods
    public static <T> ResponseWrapper<T> success(T data, String message) {
        return new ResponseWrapper<>(true, data, message, Collections.emptyList(),
                LocalDateTime.now(), generateTraceId());
    }

    public static <T> ResponseWrapper<T> success(T data) {
        return success(data, "Operación exitosa");
    }

    public static <T> ResponseWrapper<T> success(String message) {
        return success(null, message);
    }

    public static <T> ResponseWrapper<T> error(String message, List<String> errors) {
        return new ResponseWrapper<>(false, null, message, errors,
                LocalDateTime.now(), generateTraceId());
    }

    public static <T> ResponseWrapper<T> error(String message) {
        return error(message, Collections.singletonList(message));
    }

    public static <T> ResponseWrapper<T> error(String message, String error) {
        return error(message, Collections.singletonList(error));
    }

    public static <T> ResponseWrapper<T> created(T data) {
        return new ResponseWrapper<>(true, data, "Recurso creado exitosamente",
                Collections.emptyList(), LocalDateTime.now(),
                generateTraceId());
    }

    public static <T> ResponseWrapper<T> updated(T data) {
        return new ResponseWrapper<>(true, data, "Recurso actualizado exitosamente",
                Collections.emptyList(), LocalDateTime.now(),
                generateTraceId());
    }

    public static <T> ResponseWrapper<T> deleted() {
        return new ResponseWrapper<>(true, null, "Recurso eliminado exitosamente",
                Collections.emptyList(), LocalDateTime.now(),
                generateTraceId());
    }

    private static String generateTraceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Agrega un traceId específico a la respuesta
     */
    public ResponseWrapper<T> withTraceId(String traceId) {
        return new ResponseWrapper<>(success, data, message, errors, timestamp, traceId);
    }
}