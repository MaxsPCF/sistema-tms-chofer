package com.genesis.flota.security.infrastructure.adapter.out.persistence.entity;

import com.genesis.flota.security.domain.model.Usuario;
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
@Table(value = "Usuario", schema = "seguridad")
public class UsuarioEntity {

    @Id
    @Column("IdUsuario")
    private UUID idUsuario;

    @Column("IdPersona")
    private UUID idPersona;

    @Column("NombreUsuario")
    private String nombreUsuario;

    @Column("Email")
    private String email;

    @Column("PasswordHash")
    private String passwordHash;

    @Column("UltimoAcceso")
    private LocalDateTime ultimoAcceso;

    @Column("IntentosFallidos")
    private Integer intentosFallidos;

    @Column("Bloqueado")
    private Boolean bloqueado;

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

    public Usuario toDomain() {
        return Usuario.builder()
                .idUsuario(idUsuario)
                .idPersona(idPersona)
                .nombreUsuario(nombreUsuario)
                .email(email)
                .passwordHash(passwordHash)
                .ultimoAcceso(ultimoAcceso)
                .intentosFallidos(intentosFallidos != null ? intentosFallidos : 0)
                .bloqueado(bloqueado != null && bloqueado)
                .activo(activo != null && activo)
                .roles(new ArrayList<>()) // Se cargan por separado
                .fechaCreacion(fechaCreacion)
                .usuarioCreacion(usuarioCreacion)
                .fechaModificacion(fechaModificacion)
                .usuarioModifica(usuarioModifica)
                .build();
    }

    public static UsuarioEntity from(Usuario usuario) {
        return UsuarioEntity.builder()
                .idUsuario(usuario.getIdUsuario())
                .idPersona(usuario.getIdPersona())
                .nombreUsuario(usuario.getNombreUsuario())
                .email(usuario.getEmail())
                .passwordHash(usuario.getPasswordHash())
                .ultimoAcceso(usuario.getUltimoAcceso())
                .intentosFallidos(usuario.getIntentosFallidos())
                .bloqueado(usuario.isBloqueado())
                .activo(usuario.isActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .usuarioCreacion(usuario.getUsuarioCreacion())
                .fechaModificacion(usuario.getFechaModificacion())
                .usuarioModifica(usuario.getUsuarioModifica())
                .build();
    }
}