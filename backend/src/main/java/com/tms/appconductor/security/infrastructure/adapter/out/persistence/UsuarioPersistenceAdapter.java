package com.tms.appconductor.security.infrastructure.adapter.out.persistence;

import com.tms.appconductor.security.domain.model.Rol;
import com.tms.appconductor.security.domain.model.Usuario;
import com.tms.appconductor.security.domain.repository.UsuarioRepository;
import com.tms.appconductor.security.infrastructure.adapter.out.persistence.entity.UsuarioEntity;

import lombok.RequiredArgsConstructor;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UsuarioPersistenceAdapter implements UsuarioRepository {
    private final DatabaseClient databaseClient;
    private final UsuarioR2dbcRepository r2dbcRepository;

    @Override
    public Mono<Usuario> findById(UUID id) {
        return r2dbcRepository.findById(id)
                .map(UsuarioEntity::toDomain);
    }

    @Override
    public Mono<Usuario> findByNombreUsuario(String nombreUsuario) {
        return r2dbcRepository.findByNombreUsuario(nombreUsuario)
                .map(UsuarioEntity::toDomain);
    }

    @Override
    public Mono<Usuario> findByEmail(String email) {
        return r2dbcRepository.findByEmail(email)
                .map(UsuarioEntity::toDomain);
    }

    @Override
    public Mono<Boolean> existsByNombreUsuario(String nombreUsuario) {
        return r2dbcRepository.existsByNombreUsuario(nombreUsuario);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return r2dbcRepository.existsByEmail(email);
    }

    @Override
    public Mono<Usuario> save(Usuario usuario) {
        UsuarioEntity entity = UsuarioEntity.from(usuario);
        return r2dbcRepository.save(entity)
                .map(UsuarioEntity::toDomain);
    }

    @Override
    public Mono<Void> updateLoginAttempts(UUID id, int attempts) {
        return databaseClient.sql(
                "UPDATE seguridad.Usuario SET IntentosFallidos = :attempts, " +
                        "FechaModificacion = :fecha WHERE IdUsuario = :id")
                .bind("attempts", attempts)
                .bind("fecha", LocalDateTime.now())
                .bind("id", id)
                .then();
    }

    @Override
    public Mono<Void> updateBlockedStatus(UUID id, boolean blocked) {
        return databaseClient.sql(
                "UPDATE seguridad.Usuario SET Bloqueado = :blocked, " +
                        "FechaModificacion = :fecha WHERE IdUsuario = :id")
                .bind("blocked", blocked)
                .bind("fecha", LocalDateTime.now())
                .bind("id", id)
                .then();
    }

    @Override
    public Mono<Usuario> findByEmailWithRoles(String email) {
        // 1. Buscar usuario usando el repositorio R2DBC existente

        return r2dbcRepository.findByEmail(email)
                .map(UsuarioEntity::toDomain)
                // 2. Cargar roles (usa el método cargarRoles que ya tienes)
                .flatMap(usuario -> cargarRoles(usuario.getIdUsuario())
                        .collectList()
                        .map(roles -> {
                            usuario.getRoles().addAll(roles);
                            return usuario;
                        }));
    }

    private Flux<Rol> cargarRoles(UUID idUsuario) {
        return databaseClient.sql("""
                SELECT r.*, a.Codigo AS CodigoAplicacion
                FROM seguridad.Rol r
                JOIN seguridad.UsuarioRol ur ON r.IdRol = ur.IdRol
                JOIN seguridad.Aplicacion a ON r.IdAplicacion = a.IdAplicacion
                WHERE ur.IdUsuario = @idUsuario
                    AND ur.Activo = 1
                    AND r.Activo = 1
                """)
                .bind("idUsuario", idUsuario)
                .map((row, metadata) -> Rol.builder()
                        .idRol(row.get("IdRol", Integer.class))
                        .idAplicacion(row.get("IdAplicacion", Integer.class))
                        .codigoAplicacion(row.get("CodigoAplicacion", String.class))
                        .nombreRol(row.get("NombreRol", String.class))
                        .descripcion(row.get("Descripcion", String.class))
                        .esAdmin(row.get("EsAdmin", Boolean.class))
                        .activo(row.get("Activo", Boolean.class))
                        .build())
                .all();
    }

}

// Interface auxiliar de Spring Data R2DBC
interface UsuarioR2dbcRepository extends
        org.springframework.data.repository.reactive.ReactiveCrudRepository<UsuarioEntity, UUID> {
    Mono<UsuarioEntity> findByNombreUsuario(String nombreUsuario);

    Mono<UsuarioEntity> findByEmail(String email);

    Mono<Boolean> existsByNombreUsuario(String nombreUsuario);

    Mono<Boolean> existsByEmail(String email);
}