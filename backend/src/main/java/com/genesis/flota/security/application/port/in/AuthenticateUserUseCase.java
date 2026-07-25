package com.genesis.flota.security.application.port.in;

import com.genesis.flota.security.application.command.LoginCommand;
import com.genesis.flota.security.application.dto.AuthResponse;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface AuthenticateUserUseCase {
    Mono<AuthResponse> authenticate(LoginCommand command);
}