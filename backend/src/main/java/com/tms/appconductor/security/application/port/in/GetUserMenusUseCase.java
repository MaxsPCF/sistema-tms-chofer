package com.tms.appconductor.security.application.port.in;

import com.tms.appconductor.security.application.dto.MenuTreeDto;
import reactor.core.publisher.Flux;
import java.util.UUID;

@FunctionalInterface

public interface GetUserMenusUseCase {
    Flux<MenuTreeDto> getUserMenus(UUID idUsuario, Integer idAplicacion);
}