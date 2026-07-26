package com.tms.appconductor.security.application.dto;

import java.util.ArrayList;
import java.util.List;

public record MenuTreeDto(
        Integer idModulo,
        Integer idModuloPadre,
        String nombreModulo,
        String icono,
        String ruta,
        Short orden,
        boolean puedeVer,
        boolean puedeCrear,
        boolean puedeEditar,
        boolean puedeEliminar,
        boolean puedeAprobar,
        boolean puedeExportar,
        List<MenuTreeDto> children) {
    public MenuTreeDto {
        if (children == null) {
            children = new ArrayList<>();
        }
    }

    public static MenuTreeDto from(com.tms.appconductor.security.domain.model.Modulo modulo) {
        List<MenuTreeDto> hijos = modulo.getChildren().stream()
                .map(MenuTreeDto::from)
                .toList();

        return new MenuTreeDto(
                modulo.getIdModulo(),
                modulo.getIdModuloPadre(),
                modulo.getNombreModulo(),
                modulo.getIcono(),
                modulo.getRuta(),
                modulo.getOrden(),
                modulo.isPuedeVer(),
                modulo.isPuedeCrear(),
                modulo.isPuedeEditar(),
                modulo.isPuedeEliminar(),
                modulo.isPuedeAprobar(),
                modulo.isPuedeExportar(),
                new ArrayList<>(hijos));
    }
}