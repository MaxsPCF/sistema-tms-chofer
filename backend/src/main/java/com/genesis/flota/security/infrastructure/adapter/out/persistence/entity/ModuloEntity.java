package com.genesis.flota.security.infrastructure.adapter.out.persistence.entity;

import com.genesis.flota.security.domain.model.Modulo;
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
@Table(value = "Modulo", schema = "seguridad")
public class ModuloEntity {

    @Id
    @Column("IdModulo")
    private Integer idModulo;

    @Column("IdAplicacion")
    private Integer idAplicacion;

    @Column("IdModuloPadre")
    private Integer idModuloPadre;

    @Column("NombreModulo")
    private String nombreModulo;

    @Column("Descripcion")
    private String descripcion;

    @Column("Icono")
    private String icono;

    @Column("Ruta")
    private String ruta;

    @Column("Orden")
    private Short orden;

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

    // Campos de permisos (vienen de la tabla Permiso mediante JOIN)
    @Builder.Default
    private Boolean puedeVer = false;

    @Builder.Default
    private Boolean puedeCrear = false;

    @Builder.Default
    private Boolean puedeEditar = false;

    @Builder.Default
    private Boolean puedeEliminar = false;

    @Builder.Default
    private Boolean puedeAprobar = false;

    @Builder.Default
    private Boolean puedeExportar = false;

    public Modulo toDomain() {
        return Modulo.builder()
                .idModulo(idModulo)
                .idAplicacion(idAplicacion)
                .idModuloPadre(idModuloPadre)
                .nombreModulo(nombreModulo)
                .descripcion(descripcion)
                .icono(icono)
                .ruta(ruta)
                .orden(orden != null ? orden : 0)
                .activo(activo != null && activo)
                .children(new ArrayList<>())
                .puedeVer(puedeVer != null && puedeVer)
                .puedeCrear(puedeCrear != null && puedeCrear)
                .puedeEditar(puedeEditar != null && puedeEditar)
                .puedeEliminar(puedeEliminar != null && puedeEliminar)
                .puedeAprobar(puedeAprobar != null && puedeAprobar)
                .puedeExportar(puedeExportar != null && puedeExportar)
                .fechaCreacion(fechaCreacion)
                .usuarioCreacion(usuarioCreacion)
                .fechaModificacion(fechaModificacion)
                .usuarioModifica(usuarioModifica)
                .build();
    }

    public static ModuloEntity from(Modulo modulo) {
        return ModuloEntity.builder()
                .idModulo(modulo.getIdModulo())
                .idAplicacion(modulo.getIdAplicacion())
                .idModuloPadre(modulo.getIdModuloPadre())
                .nombreModulo(modulo.getNombreModulo())
                .descripcion(modulo.getDescripcion())
                .icono(modulo.getIcono())
                .ruta(modulo.getRuta())
                .orden(modulo.getOrden())
                .activo(modulo.isActivo())
                .fechaCreacion(modulo.getFechaCreacion())
                .usuarioCreacion(modulo.getUsuarioCreacion())
                .fechaModificacion(modulo.getFechaModificacion())
                .usuarioModifica(modulo.getUsuarioModifica())
                .build();
    }
}