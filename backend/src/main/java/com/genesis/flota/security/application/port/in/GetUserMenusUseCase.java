package com.genesis.flota.security.application.port.in;

import com.genesis.flota.security.application.dto.MenuTreeDto;
import reactor.core.publisher.Flux;

import java.util.UUID;

@FunctionalInterface
public interface GetUserMenusUseCase {
    Flux<MenuTreeDto> getUserMenus(UUID idUsuario, Integer idAplicacion);
}