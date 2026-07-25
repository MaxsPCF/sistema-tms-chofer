package com.genesis.flota.shared.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO para detalles de error")
public record ErrorDetailDTO(

        @Schema(description = "Código de error único")
        String errorCode,

        @Schema(description = "Mensaje de error")
        String message,

        @Schema(description = "Detalles adicionales del error")
        List<String> details,

        @Schema(description = "Ruta donde ocurrió el error")
        String path,

        @Schema(description = "Timestamp del error")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime timestamp,

        @Schema(description = "ID de trazabilidad")
        String traceId
) {
    public static ErrorDetailDTO of(String errorCode, String message, String path, String traceId) {
        return new ErrorDetailDTO(errorCode, message, List.of(), path, LocalDateTime.now(), traceId);
    }

    public static ErrorDetailDTO of(String errorCode, String message, List<String> details,
                                    String path, String traceId) {
        return new ErrorDetailDTO(errorCode, message, details, path, LocalDateTime.now(), traceId);
    }
}