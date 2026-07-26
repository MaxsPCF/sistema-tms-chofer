package com.tms.appconductor.security.application.port.in;

import com.tms.appconductor.security.application.command.LoginCommand;
import com.tms.appconductor.security.application.dto.AuthResponse;
import reactor.core.publisher.Mono;

@FunctionalInterface

public interface AuthenticateUserUseCase {
    Mono<AuthResponse> authenticate(LoginCommand command);
}