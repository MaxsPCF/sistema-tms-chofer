package com.tms.appconductor.shared.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "DTO para solicitudes paginadas")
public record PageRequestDTO(
        @Schema(description = "Número de página (0-based)", example = "0", defaultValue = "0") @Min(value = 0, message = "La página no puede ser negativa") int page,

        @Schema(description = "Tamaño de página", example = "20", defaultValue = "20") @Min(value = 1, message = "El tamaño mínimo es 1") @Max(value = 100, message = "El tamaño máximo es 100") int size,

        @Schema(description = "Campo para ordenar", example = "fechaCreacion") String sort,

        @Schema(description = "Dirección del ordenamiento (ASC/DESC)", example = "DESC") String direction) {

    public PageRequestDTO {
        if (page < 0)
            page = 0;

        if (size < 1)
            size = 20;

        if (size > 100)
            size = 100;

        if (sort == null || sort.isBlank())
            sort = "fechaCreacion";

        if (direction == null || direction.isBlank())
            direction = "DESC";
    }

    public static PageRequestDTO defaultPage() {
        return new PageRequestDTO(0, 20, "fechaCreacion", "DESC");
    }

    public long getOffset() {
        return (long) page * size;
    }
}