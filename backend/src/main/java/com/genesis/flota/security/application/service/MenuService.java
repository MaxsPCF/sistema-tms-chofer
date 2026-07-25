package com.genesis.flota.security.application.service;

import com.genesis.flota.security.application.dto.MenuTreeDto;
import com.genesis.flota.security.application.dto.ModuloDto;
import com.genesis.flota.security.application.port.in.GetUserMenusUseCase;
import com.genesis.flota.security.application.query.GetUserMenusResult;
import com.genesis.flota.security.domain.model.Modulo;
import com.genesis.flota.security.domain.repository.ModuloRepository;
import com.genesis.flota.security.domain.service.MenuBuilderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService implements GetUserMenusUseCase {

    private final ModuloRepository moduloRepository;
    private final MenuBuilderService menuBuilderService;

    @Override
    public Flux<MenuTreeDto> getUserMenus(UUID idUsuario, Integer idAplicacion) {
        log.debug("Obteniendo menús para usuario: {} en aplicación: {}",
                idUsuario, idAplicacion);

        Flux<Modulo> modulosFlux;

        if (idAplicacion != null) {
            modulosFlux = moduloRepository
                    .findMenusByUsuarioIdAndAplicacionId(idUsuario, idAplicacion);
        } else {
            modulosFlux = moduloRepository.findMenusByUsuarioId(idUsuario);
        }

        return modulosFlux
                .collectList()
                .map(menuBuilderService::construirArbolMenus)
                .flatMapMany(Flux::fromIterable)
                .map(MenuTreeDto::from)
                .doOnComplete(() -> log.debug("Menús cargados exitosamente para usuario: {}",
                        idUsuario))
                .doOnError(error -> log.error("Error cargando menús para usuario: {}",
                        idUsuario, error));
    }

    /**
     * Obtiene menús planos (sin estructura de árbol)
     */
    public Flux<ModuloDto> getFlatMenus(UUID idUsuario, Integer idAplicacion) {
        Flux<Modulo> modulosFlux;

        if (idAplicacion != null) {
            modulosFlux = moduloRepository
                    .findMenusByUsuarioIdAndAplicacionId(idUsuario, idAplicacion);
        } else {
            modulosFlux = moduloRepository.findMenusByUsuarioId(idUsuario);
        }

        return modulosFlux.map(ModuloDto::from);
    }

    /**
     * Obtiene menús con resultado enriquecido (incluye metadata)
     */
    public Mono<GetUserMenusResult> getUserMenusWithMetadata(
            UUID idUsuario, Integer idAplicacion) {

        return getUserMenus(idUsuario, idAplicacion)
                .collectList()
                .map(menus -> {
                    String appName = idAplicacion != null ?
                            "Aplicación " + idAplicacion : "Todas las aplicaciones";
                    return GetUserMenusResult.of(menus, appName);
                });
    }
}