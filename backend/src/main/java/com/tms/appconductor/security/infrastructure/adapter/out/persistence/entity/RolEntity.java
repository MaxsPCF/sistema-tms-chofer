package com.tms.appconductor.security.infrastructure.adapter.out.persistence.entity;

import com.tms.appconductor.security.domain.model.Rol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "Rol", schema = "seguridad")
public class RolEntity {
    @Id
    @Column("IdRol")
    private Integer idRol;

    @Column("IdAplicacion")
    private Integer idAplicacion;

    @Column("NombreRol")
    private String nombreRol;

    @Column("Descripcion")
    private String descripcion;

    @Column("EsAdmin")
    private Boolean esAdmin;

    @Column("Activo")
    private Boolean activo;

    @Column("FechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column("UsuarioCreacion")
    private UUID usuarioCreacion;

    @Column("FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column("UsuarioModifica")
    private UUID usuarioModifica;

    public Rol toDomain() {
        return Rol.builder()
                .idRol(idRol)
                .idAplicacion(idAplicacion)
                .nombreRol(nombreRol)
                .descripcion(descripcion)
                .esAdmin(esAdmin != null && esAdmin)
                .activo(activo != null && activo)
                .permisos(new ArrayList<>()) // Se cargan por separado
                .fechaCreacion(fechaCreacion)
                .usuarioCreacion(usuarioCreacion)
                .build();
    }

    public static RolEntity from(Rol rol) {
        return RolEntity.builder()
                .idRol(rol.getIdRol())
                .idAplicacion(rol.getIdAplicacion())
                .nombreRol(rol.getNombreRol())
                .descripcion(rol.getDescripcion())
                .esAdmin(rol.isEsAdmin())
                .activo(rol.isActivo())
                .fechaCreacion(rol.getFechaCreacion())
                .usuarioCreacion(rol.getUsuarioCreacion())
                .fechaModificacion(LocalDateTime.now())
                .build();
    }
}