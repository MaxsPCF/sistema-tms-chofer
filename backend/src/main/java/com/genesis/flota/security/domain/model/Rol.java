package com.genesis.flota.security.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Rol {
    private Integer idRol;
    private Integer idAplicacion;
    private String codigoAplicacion;  // ADMIN_WEB, PORTAL_CLIENTE, APP_CONDUCTOR
    private String nombreRol;
    private String descripcion;
    private boolean esAdmin;
    private boolean activo;
    private List<Permiso> permisos;
    private LocalDateTime fechaCreacion;
    private UUID usuarioCreacion;
}