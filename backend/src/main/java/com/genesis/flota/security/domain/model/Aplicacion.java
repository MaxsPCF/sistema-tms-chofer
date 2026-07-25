package com.genesis.flota.security.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class Aplicacion {
    private Integer idAplicacion;
    private String codigo;
    private String nombre;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private UUID usuarioCreacion;

    // Códigos de aplicación predefinidos
    public static final String ADMIN_WEB = "ADMIN_WEB";
    public static final String PORTAL_CLIENTE = "PORTAL_CLIENTE";
    public static final String APP_CONDUCTOR = "APP_CONDUCTOR";

    // Comportamiento del dominio
    public boolean esAdminWeb() {
        return ADMIN_WEB.equals(codigo);
    }

    public boolean esPortalCliente() {
        return PORTAL_CLIENTE.equals(codigo);
    }

    public boolean esAppConductor() {
        return APP_CONDUCTOR.equals(codigo);
    }

    public boolean estaActiva() {
        return activo;
    }

    public void desactivar() {
        if (esAdminWeb()) {
            throw new IllegalStateException(
                    "No se puede desactivar la aplicación de administración");
        }
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
    }
}