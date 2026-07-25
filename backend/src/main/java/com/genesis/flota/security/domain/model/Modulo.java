package com.genesis.flota.security.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Modulo {
    private Integer idModulo;
    private Integer idAplicacion;
    private Integer idModuloPadre;
    private String nombreModulo;
    private String descripcion;
    private String icono;
    private String ruta;
    private Short orden;
    private boolean activo;

    @Builder.Default
    private List<Modulo> children = new ArrayList<>();

    // Permisos específicos para este módulo
    private boolean puedeVer;
    private boolean puedeCrear;
    private boolean puedeEditar;
    private boolean puedeEliminar;
    private boolean puedeAprobar;
    private boolean puedeExportar;

    private LocalDateTime fechaCreacion;
    private UUID usuarioCreacion;
    private LocalDateTime fechaModificacion;
    private UUID usuarioModifica;

    // Métodos de dominio
    public void agregarHijo(Modulo hijo) {
        this.children.add(hijo);
    }

    public boolean esRaiz() {
        return idModuloPadre == null;
    }

    public boolean esMenu() {
        return ruta != null && !ruta.isEmpty();
    }
}