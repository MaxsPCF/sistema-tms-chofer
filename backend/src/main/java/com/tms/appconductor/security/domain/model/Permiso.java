package com.tms.appconductor.security.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Permiso {
    private UUID idPermiso;
    private Integer idRol;
    private Integer idModulo;
    private boolean puedeVer;
    private boolean puedeCrear;
    private boolean puedeEditar;
    private boolean puedeEliminar;
    private boolean puedeAprobar;
    private boolean puedeExportar;
    private boolean activo;
}