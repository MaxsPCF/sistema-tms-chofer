package com.tms.appconductor.security.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UsuarioRol {
    private UUID idUsuarioRol;
    private UUID idUsuario;
    private Integer idRol;
    private LocalDateTime fechaAsignacion;
    private LocalDate fechaVigencia;
    private UUID usuarioAsigna;
    private boolean activo;

    // Comportamiento del dominio

    public boolean estaVigente() {
        if (fechaVigencia == null) {
            return activo;
        }

        return activo && !LocalDate.now().isAfter(fechaVigencia);
    }

    public void desactivar() {
        this.activo = false;
    }

    public void extenderVigencia(LocalDate nuevaFecha) {
        if (nuevaFecha != null && nuevaFecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "La fecha de vigencia no puede ser anterior a hoy");
        }

        this.fechaVigencia = nuevaFecha;
    }

    public boolean esAsignacionPermanente() {
        return fechaVigencia == null;
    }
}