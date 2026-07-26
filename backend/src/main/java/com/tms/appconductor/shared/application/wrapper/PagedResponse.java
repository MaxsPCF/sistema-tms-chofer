package com.tms.appconductor.shared.application.wrapper;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Respuesta paginada para listados")
public record PagedResponse<T>(

        @Schema(description = "Lista de elementos en la página actual") List<T> content,

        @Schema(description = "Número de página actual (0-based)", example = "0") int page,

        @Schema(description = "Tamaño de página", example = "20") int size,

        @Schema(description = "Total de elementos", example = "150") long totalElements,

        @Schema(description = "Total de páginas", example = "8") int totalPages,

        @Schema(description = "Indica si es la primera página") boolean first,

        @Schema(description = "Indica si es la última página") boolean last,

        @Schema(description = "Indica si hay contenido") boolean hasContent) {

    public static <T> PagedResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return new PagedResponse<>(
                content,
                page,
                size,
                totalElements,
                totalPages,
                page == 0,
                page >= totalPages - 1,
                !content.isEmpty());
    }

    public static <T> PagedResponse<T> empty(int page, int size) {
        return new PagedResponse<>(
                List.of(), page, size, 0, 0, true, true, false);
    }
}