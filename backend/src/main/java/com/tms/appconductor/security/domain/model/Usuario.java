package com.tms.appconductor.security.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Usuario {
    private UUID idUsuario;
    private UUID idPersona;
    private String nombreUsuario;
    private String email;
    private String passwordHash;
    private LocalDateTime ultimoAcceso;
    private int intentosFallidos;
    private boolean bloqueado;
    private boolean activo;

    @Builder.Default
    private List<Rol> roles = new ArrayList<>();

    private LocalDateTime fechaCreacion;
    private UUID usuarioCreacion;
    private LocalDateTime fechaModificacion;
    private UUID usuarioModifica;

    // Comportamiento del dominio

    public boolean estaBloqueado() {
        return bloqueado;
    }

    public boolean estaActivo() {
        return activo;
    }

    public void incrementarIntentosFallidos() {
        this.intentosFallidos++;
    }

    public void bloquear() {
        this.bloqueado = true;
    }

    public void desbloquear() {
        this.bloqueado = false;
        this.intentosFallidos = 0;
    }

    public void registrarAccesoExitoso() {
        this.ultimoAcceso = LocalDateTime.now();
        this.intentosFallidos = 0;
    }

    public boolean tieneRol(String nombreRol) {
        return roles.stream()
                .anyMatch(rol -> rol.getNombreRol().equals(nombreRol) && rol.isActivo());
    }

    public List<String> getNombresRoles() {
        return roles.stream()
                .filter(Rol::isActivo)
                .map(Rol::getNombreRol)
                .toList();
    }
}