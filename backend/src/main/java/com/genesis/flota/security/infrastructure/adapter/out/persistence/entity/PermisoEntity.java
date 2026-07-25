package com.genesis.flota.security.infrastructure.adapter.out.persistence.entity;

import com.genesis.flota.security.domain.model.Permiso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "Permiso", schema = "seguridad")
public class PermisoEntity {

    @Id
    @Column("IdPermiso")
    private UUID idPermiso;

    @Column("IdRol")
    private Integer idRol;

    @Column("IdModulo")
    private Integer idModulo;

    @Column("PuedeVer")
    private Boolean puedeVer;

    @Column("PuedeCrear")
    private Boolean puedeCrear;

    @Column("PuedeEditar")
    private Boolean puedeEditar;

    @Column("PuedeEliminar")
    private Boolean puedeEliminar;

    @Column("PuedeAprobar")
    private Boolean puedeAprobar;

    @Column("PuedeExportar")
    private Boolean puedeExportar;

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

    public Permiso toDomain() {
        return Permiso.builder()
                .idPermiso(idPermiso)
                .idRol(idRol)
                .idModulo(idModulo)
                .puedeVer(puedeVer != null && puedeVer)
                .puedeCrear(puedeCrear != null && puedeCrear)
                .puedeEditar(puedeEditar != null && puedeEditar)
                .puedeEliminar(puedeEliminar != null && puedeEliminar)
                .puedeAprobar(puedeAprobar != null && puedeAprobar)
                .puedeExportar(puedeExportar != null && puedeExportar)
                .activo(activo != null && activo)
                .build();
    }

    public static PermisoEntity from(Permiso permiso) {
        return PermisoEntity.builder()
                .idPermiso(permiso.getIdPermiso())
                .idRol(permiso.getIdRol())
                .idModulo(permiso.getIdModulo())
                .puedeVer(permiso.isPuedeVer())
                .puedeCrear(permiso.isPuedeCrear())
                .puedeEditar(permiso.isPuedeEditar())
                .puedeEliminar(permiso.isPuedeEliminar())
                .puedeAprobar(permiso.isPuedeAprobar())
                .puedeExportar(permiso.isPuedeExportar())
                .activo(permiso.isActivo())
                .build();
    }
}