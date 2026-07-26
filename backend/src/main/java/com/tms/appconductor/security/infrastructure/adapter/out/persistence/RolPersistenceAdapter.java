package com.tms.appconductor.security.infrastructure.adapter.out.persistence;

import com.tms.appconductor.security.domain.model.Rol;
import com.tms.appconductor.security.domain.repository.RolRepository;
import com.tms.appconductor.security.infrastructure.adapter.out.persistence.entity.RolEntity;

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
public class RolPersistenceAdapter implements RolRepository {
    private final DatabaseClient databaseClient;

    @Override
    public Mono<Rol> findById(Integer idRol) {
        return databaseClient.sql(
                "SELECT * FROM seguridad.Rol WHERE IdRol = :idRol AND Activo = 1")
                .bind("idRol", idRol)
                .map((row, metadata) -> buildRolEntity(row))
                .one()
                .map(RolEntity::toDomain);
    }

    @Override
    public Mono<Rol> findByNombreRol(String nombreRol) {
        return databaseClient.sql(
                "SELECT * FROM seguridad.Rol WHERE NombreRol = :nombreRol AND Activo = 1")
                .bind("nombreRol", nombreRol)
                .map((row, metadata) -> buildRolEntity(row))
                .one()
                .map(RolEntity::toDomain);
    }

    @Override
    public Flux<Rol> findByUsuarioId(UUID idUsuario) {

        String sql = """
                SELECT r.* FROM seguridad.Rol r
                INNER JOIN seguridad.UsuarioRol ur ON r.IdRol = ur.IdRol
                WHERE ur.IdUsuario = :idUsuario
                    AND ur.Activo = 1
                    AND r.Activo = 1
                """;

        return databaseClient.sql(sql)
                .bind("idUsuario", idUsuario)
                .map((row, metadata) -> buildRolEntity(row))
                .all()
                .map(RolEntity::toDomain)
                .doOnComplete(() -> log.debug("Roles cargados para usuario: {}", idUsuario));
    }

    @Override
    public Flux<Rol> findByAplicacionId(Integer idAplicacion) {
        return databaseClient.sql(
                "SELECT * FROM seguridad.Rol WHERE IdAplicacion = :idAplicacion AND Activo = 1")
                .bind("idAplicacion", idAplicacion)
                .map((row, metadata) -> buildRolEntity(row))
                .all()
                .map(RolEntity::toDomain);
    }

    @Override
    public Flux<Rol> findActiveRolesByUsuarioId(UUID idUsuario) {
        return findByUsuarioId(idUsuario);
    }

    @Override
    public Mono<Boolean> existsByNombreRol(String nombreRol) {
        return databaseClient.sql(
                "SELECT COUNT(1) as count FROM seguridad.Rol WHERE NombreRol = :nombreRol")
                .bind("nombreRol", nombreRol)
                .map((row, metadata) -> row.get("count", Long.class))
                .one()
                .map(count -> count > 0);
    }

    @Override
    public Mono<Rol> save(Rol rol) {
        if (rol.getIdRol() == null) {
            return insert(rol);
        }

        return update(rol);
    }

    @Override
    public Mono<Void> deleteById(Integer idRol) {
        return databaseClient.sql(
                "UPDATE seguridad.Rol SET Activo = 0 WHERE IdRol = :idRol")
                .bind("idRol", idRol)
                .then();
    }

    private Mono<Rol> insert(Rol rol) {
        return databaseClient.sql("""
                INSERT INTO seguridad.Rol (IdAplicacion, NombreRol, Descripcion, EsAdmin, Activo)
                VALUES (:idAplicacion, :nombreRol, :descripcion, :esAdmin, :activo)
                """)
                .bind("idAplicacion", rol.getIdAplicacion())
                .bind("nombreRol", rol.getNombreRol())
                .bind("descripcion", rol.getDescripcion())
                .bind("esAdmin", rol.isEsAdmin())
                .bind("activo", rol.isActivo())
                .then()
                .then(findByNombreRol(rol.getNombreRol()));
    }

    private Mono<Rol> update(Rol rol) {
        return databaseClient.sql("""
                UPDATE seguridad.Rol
                SET NombreRol = :nombreRol, Descripcion = :descripcion,
                    EsAdmin = :esAdmin, Activo = :activo,
                    FechaModificacion = GETUTCDATE()
                WHERE IdRol = :idRol
                """)
                .bind("idRol", rol.getIdRol())
                .bind("nombreRol", rol.getNombreRol())
                .bind("descripcion", rol.getDescripcion())
                .bind("esAdmin", rol.isEsAdmin())
                .bind("activo", rol.isActivo())
                .then()
                .then(findById(rol.getIdRol()));
    }

    private RolEntity buildRolEntity(io.r2dbc.spi.Row row) {
        return RolEntity.builder()
                .idRol(row.get("IdRol", Integer.class))
                .idAplicacion(row.get("IdAplicacion", Integer.class))
                .nombreRol(row.get("NombreRol", String.class))
                .descripcion(row.get("Descripcion", String.class))
                .esAdmin(row.get("EsAdmin", Boolean.class))
                .activo(row.get("Activo", Boolean.class))
                .fechaCreacion(row.get("FechaCreacion", java.time.LocalDateTime.class))
                .usuarioCreacion(row.get("UsuarioCreacion", UUID.class))
                .build();
    }
}