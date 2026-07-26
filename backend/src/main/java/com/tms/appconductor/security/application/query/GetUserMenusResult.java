package com.tms.appconductor.security.application.query;

import com.tms.appconductor.security.application.dto.MenuTreeDto;
import java.util.List;

/**
 * 
 * Resultado de la consulta de menús de usuario.
 * 
 * Separa la query del resultado para mantener CQRS puro.
 * 
 */
public record GetUserMenusResult(
        List<MenuTreeDto> menus,
        int totalMenus,
        int menusRaiz,
        String aplicacionNombre) {

    public GetUserMenusResult {
        if (menus == null) {
            menus = List.of();
        }

        totalMenus = menus.size();
        menusRaiz = (int) menus.stream()
                .filter(m -> m.idModuloPadre() == null)
                .count();
    }

    public static GetUserMenusResult of(List<MenuTreeDto> menus, String aplicacionNombre) {
        return new GetUserMenusResult(menus, menus.size(), 0, aplicacionNombre);
    }

    public boolean tieneMenus() {
        return !menus.isEmpty();
    }
}