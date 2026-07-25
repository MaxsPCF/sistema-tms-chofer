package com.genesis.flota.security.infrastructure.adapter.out.persistence.entity;

import com.genesis.flota.security.domain.model.UsuarioRol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "UsuarioRol", schema = "seguridad")
public class UsuarioRolEntity {

    @Id
    @Column("IdUsuarioRol")
    private UUID idUsuarioRol;

    @Column("IdUsuario")
    private UUID idUsuario;

    @Column("IdRol")
    private Integer idRol;

    @Column("FechaAsignacion")
    private LocalDateTime fechaAsignacion;

    @Column("FechaVigencia")
    private LocalDate fechaVigencia;

    @Column("UsuarioAsigna")
    private UUID usuarioAsigna;

    @Column("Activo")
    private Boolean activo;

    public UsuarioRol toDomain() {
        return UsuarioRol.builder()
                .idUsuarioRol(idUsuarioRol)
                .idUsuario(idUsuario)
                .idRol(idRol)
                .fechaAsignacion(fechaAsignacion)
                .fechaVigencia(fechaVigencia)
                .usuarioAsigna(usuarioAsigna)
                .activo(activo != null && activo)
                .build();
    }

    public static UsuarioRolEntity from(UsuarioRol usuarioRol) {
        return UsuarioRolEntity.builder()
                .idUsuarioRol(usuarioRol.getIdUsuarioRol())
                .idUsuario(usuarioRol.getIdUsuario())
                .idRol(usuarioRol.getIdRol())
                .fechaAsignacion(usuarioRol.getFechaAsignacion())
                .fechaVigencia(usuarioRol.getFechaVigencia())
                .usuarioAsigna(usuarioRol.getUsuarioAsigna())
                .activo(usuarioRol.isActivo())
                .build();
    }
}