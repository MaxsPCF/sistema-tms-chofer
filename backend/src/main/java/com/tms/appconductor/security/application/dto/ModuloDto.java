package com.tms.appconductor.security.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para representar un módulo del sistema")
public record ModuloDto(
        @Schema(description = "ID único del módulo", example = "1057") Integer idModulo,

        @Schema(description = "ID del módulo padre (null si es raíz)", example = "null") Integer idModuloPadre,

        @Schema(description = "Nombre del módulo", example = "Dashboard") String nombreModulo,

        @Schema(description = "Descripción del módulo") String descripcion,

        @Schema(description = "Ícono del módulo (Material Icons)", example = "dashboard") String icono,

        @Schema(description = "Ruta del módulo", example = "/seguridad/dashboard") String ruta,

        @Schema(description = "Orden de visualización", example = "0") Short orden,

        @Schema(description = "Indica si el módulo está activo") boolean activo,

        @Schema(description = "Permiso de visualización") boolean puedeVer,

        @Schema(description = "Permiso de creación") boolean puedeCrear,

        @Schema(description = "Permiso de edición") boolean puedeEditar,

        @Schema(description = "Permiso de eliminación") boolean puedeEliminar,

        @Schema(description = "Permiso de aprobación") boolean puedeAprobar,

        @Schema(description = "Permiso de exportación") boolean puedeExportar) {
    /**
     * 
     * Crea un ModuloDto desde el modelo de dominio
     * 
     */
    public static ModuloDto from(com.tms.appconductor.security.domain.model.Modulo modulo) {
        return new ModuloDto(
                modulo.getIdModulo(),
                modulo.getIdModuloPadre(),
                modulo.getNombreModulo(),
                modulo.getDescripcion(),
                modulo.getIcono(),
                modulo.getRuta(),
                modulo.getOrden(),
                modulo.isActivo(),
                modulo.isPuedeVer(),
                modulo.isPuedeCrear(),
                modulo.isPuedeEditar(),
                modulo.isPuedeEliminar(),
                modulo.isPuedeAprobar(),
                modulo.isPuedeExportar());
    }

    /**
     * 
     * Verifica si el módulo tiene al menos un permiso activo
     * 
     */
    public boolean tieneAlgunPermiso() {
        return puedeVer || puedeCrear || puedeEditar ||
                puedeEliminar || puedeAprobar || puedeExportar;
    }
}