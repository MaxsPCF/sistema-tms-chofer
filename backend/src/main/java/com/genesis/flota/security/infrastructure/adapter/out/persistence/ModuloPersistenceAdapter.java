package com.genesis.flota.security.infrastructure.adapter.out.persistence;

import com.genesis.flota.security.domain.model.Modulo;
import com.genesis.flota.security.domain.repository.ModuloRepository;
import com.genesis.flota.security.infrastructure.adapter.out.persistence.entity.ModuloEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ModuloPersistenceAdapter implements ModuloRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<Modulo> findMenusByUsuarioId(UUID idUsuario) {
        log.debug("Consultando menús para usuario: {}", idUsuario);

        String sql = """
            SELECT DISTINCT 
                m.IdModulo, m.IdAplicacion, m.IdModuloPadre, 
                m.NombreModulo, m.Descripcion, m.Icono, m.Ruta, 
                m.Orden, m.Activo, m.FechaCreacion, m.UsuarioCreacion,
                p.PuedeVer, p.PuedeCrear, p.PuedeEditar, 
                p.PuedeEliminar, p.PuedeAprobar, p.PuedeExportar
            FROM seguridad.Modulo m
            INNER JOIN seguridad.Permiso p ON m.IdModulo = p.IdModulo
            INNER JOIN seguridad.Rol r ON p.IdRol = r.IdRol
            INNER JOIN seguridad.UsuarioRol ur ON r.IdRol = ur.IdRol
            WHERE ur.IdUsuario = :idUsuario
                AND ur.Activo = 1
                AND r.Activo = 1
                AND m.Activo = 1
                AND p.Activo = 1
            ORDER BY m.Orden, m.IdModuloPadre
            """;

        return databaseClient.sql(sql)
                .bind("idUsuario", idUsuario)
                .map((row, metadata) -> ModuloEntity.builder()
                        .idModulo(row.get("IdModulo", Integer.class))
                        .idAplicacion(row.get("IdAplicacion", Integer.class))
                        .idModuloPadre(row.get("IdModuloPadre", Integer.class))
                        .nombreModulo(row.get("NombreModulo", String.class))
                        .descripcion(row.get("Descripcion", String.class))
                        .icono(row.get("Icono", String.class))
                        .ruta(row.get("Ruta", String.class))
                        .orden(row.get("Orden", Short.class))
                        .activo(row.get("Activo", Boolean.class))
                        .puedeVer(row.get("PuedeVer", Boolean.class))
                        .puedeCrear(row.get("PuedeCrear", Boolean.class))
                        .puedeEditar(row.get("PuedeEditar", Boolean.class))
                        .puedeEliminar(row.get("PuedeEliminar", Boolean.class))
                        .puedeAprobar(row.get("PuedeAprobar", Boolean.class))
                        .puedeExportar(row.get("PuedeExportar", Boolean.class))
                        .build())
                .all()
                .map(ModuloEntity::toDomain)
                .doOnComplete(() -> log.debug("Menús cargados para usuario: {}", idUsuario));
    }

    @Override
    public Flux<Modulo> findMenusByUsuarioIdAndAplicacionId(UUID idUsuario, Integer idAplicacion) {
        log.debug("Consultando menús para usuario: {} en aplicación: {}",
                idUsuario, idAplicacion);

        String sql = """
            SELECT DISTINCT 
                m.IdModulo, m.IdAplicacion, m.IdModuloPadre, 
                m.NombreModulo, m.Descripcion, m.Icono, m.Ruta, 
                m.Orden, m.Activo, m.FechaCreacion, m.UsuarioCreacion,
                p.PuedeVer, p.PuedeCrear, p.PuedeEditar, 
                p.PuedeEliminar, p.PuedeAprobar, p.PuedeExportar
            FROM seguridad.Modulo m
            INNER JOIN seguridad.Permiso p ON m.IdModulo = p.IdModulo
            INNER JOIN seguridad.Rol r ON p.IdRol = r.IdRol
            INNER JOIN seguridad.UsuarioRol ur ON r.IdRol = ur.IdRol
            WHERE ur.IdUsuario = :idUsuario
                AND m.IdAplicacion = :idAplicacion
                AND ur.Activo = 1
                AND r.Activo = 1
                AND m.Activo = 1
                AND p.Activo = 1
            ORDER BY m.Orden, m.IdModuloPadre
            """;

        return databaseClient.sql(sql)
                .bind("idUsuario", idUsuario)
                .bind("idAplicacion", idAplicacion)
                .map((row, metadata) -> ModuloEntity.builder()
                        .idModulo(row.get("IdModulo", Integer.class))
                        .idAplicacion(row.get("IdAplicacion", Integer.class))
                        .idModuloPadre(row.get("IdModuloPadre", Integer.class))
                        .nombreModulo(row.get("NombreModulo", String.class))
                        .descripcion(row.get("Descripcion", String.class))
                        .icono(row.get("Icono", String.class))
                        .ruta(row.get("Ruta", String.class))
                        .orden(row.get("Orden", Short.class))
                        .activo(row.get("Activo", Boolean.class))
                        .puedeVer(row.get("PuedeVer", Boolean.class))
                        .puedeCrear(row.get("PuedeCrear", Boolean.class))
                        .puedeEditar(row.get("PuedeEditar", Boolean.class))
                        .puedeEliminar(row.get("PuedeEliminar", Boolean.class))
                        .puedeAprobar(row.get("PuedeAprobar", Boolean.class))
                        .puedeExportar(row.get("PuedeExportar", Boolean.class))
                        .build())
                .all()
                .map(ModuloEntity::toDomain)
                .doOnComplete(() -> log.debug(
                        "Menús cargados para usuario: {} en app: {}",
                        idUsuario, idAplicacion));
    }

    @Override
    public Flux<Modulo> findByAplicacionId(Integer idAplicacion) {
        return databaseClient.sql(
                        "SELECT * FROM seguridad.Modulo WHERE IdAplicacion = :idAplicacion AND Activo = 1")
                .bind("idAplicacion", idAplicacion)
                .map((row, metadata) -> ModuloEntity.builder()
                        .idModulo(row.get("IdModulo", Integer.class))
                        .idAplicacion(row.get("IdAplicacion", Integer.class))
                        .idModuloPadre(row.get("IdModuloPadre", Integer.class))
                        .nombreModulo(row.get("NombreModulo", String.class))
                        .descripcion(row.get("Descripcion", String.class))
                        .icono(row.get("Icono", String.class))
                        .ruta(row.get("Ruta", String.class))
                        .orden(row.get("Orden", Short.class))
                        .activo(row.get("Activo", Boolean.class))
                        .build())
                .all()
                .map(ModuloEntity::toDomain);
    }

    @Override
    public Mono<Modulo> findById(Integer idModulo) {
        return databaseClient.sql(
                        "SELECT * FROM seguridad.Modulo WHERE IdModulo = :idModulo")
                .bind("idModulo", idModulo)
                .map((row, metadata) -> ModuloEntity.builder()
                        .idModulo(row.get("IdModulo", Integer.class))
                        .idAplicacion(row.get("IdAplicacion", Integer.class))
                        .idModuloPadre(row.get("IdModuloPadre", Integer.class))
                        .nombreModulo(row.get("NombreModulo", String.class))
                        .descripcion(row.get("Descripcion", String.class))
                        .icono(row.get("Icono", String.class))
                        .ruta(row.get("Ruta", String.class))
                        .orden(row.get("Orden", Short.class))
                        .activo(row.get("Activo", Boolean.class))
                        .build())
                .one()
                .map(ModuloEntity::toDomain);
    }

    @Override
    public Flux<Modulo> findMenusByUsuarioIdAndAplicacionCodigo(UUID idUsuario, String codigoAplicacion) {
        log.debug("Menús para usuario: {} en app: {}", idUsuario, codigoAplicacion);

        String sql = """
        SELECT DISTINCT 
            m.IdModulo, m.IdAplicacion, m.IdModuloPadre, 
            m.NombreModulo, m.Descripcion, m.Icono, m.Ruta, 
            m.Orden, m.Activo,
            p.PuedeVer, p.PuedeCrear, p.PuedeEditar, 
            p.PuedeEliminar, p.PuedeAprobar, p.PuedeExportar
        FROM seguridad.Modulo m
        INNER JOIN seguridad.Aplicacion a ON m.IdAplicacion = a.IdAplicacion
        LEFT JOIN seguridad.Permiso p ON m.IdModulo = p.IdModulo AND p.Activo = 1
        LEFT JOIN seguridad.Rol r ON p.IdRol = r.IdRol AND r.Activo = 1
        LEFT JOIN seguridad.UsuarioRol ur ON r.IdRol = ur.IdRol 
            AND ur.IdUsuario = @idUsuario AND ur.Activo = 1
        WHERE a.Codigo = @codigoAplicacion
            AND m.Activo = 1
        ORDER BY m.Orden, m.IdModuloPadre
        """;

        return databaseClient.sql(sql)
                .bind("idUsuario", idUsuario)
                .bind("codigoAplicacion", codigoAplicacion)
                .map((row, metadata) -> ModuloEntity.builder()
                        .idModulo(row.get("IdModulo", Integer.class))
                        .idAplicacion(row.get("IdAplicacion", Integer.class))
                        .idModuloPadre(row.get("IdModuloPadre", Integer.class))
                        .nombreModulo(row.get("NombreModulo", String.class))
                        .descripcion(row.get("Descripcion", String.class))
                        .icono(row.get("Icono", String.class))
                        .ruta(row.get("Ruta", String.class))
                        .orden(row.get("Orden", Short.class))
                        .activo(row.get("Activo", Boolean.class))
                        .puedeVer(row.get("PuedeVer", Boolean.class) != null && row.get("PuedeVer", Boolean.class))
                        .puedeCrear(row.get("PuedeCrear", Boolean.class) != null && row.get("PuedeCrear", Boolean.class))
                        .puedeEditar(row.get("PuedeEditar", Boolean.class) != null && row.get("PuedeEditar", Boolean.class))
                        .puedeEliminar(row.get("PuedeEliminar", Boolean.class) != null && row.get("PuedeEliminar", Boolean.class))
                        .puedeAprobar(row.get("PuedeAprobar", Boolean.class) != null && row.get("PuedeAprobar", Boolean.class))
                        .puedeExportar(row.get("PuedeExportar", Boolean.class) != null && row.get("PuedeExportar", Boolean.class))
                        .build())
                .all()
                .map(ModuloEntity::toDomain);
    }
}