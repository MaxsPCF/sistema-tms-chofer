package com.tms.appconductor.security.application.port.in;

import reactor.core.publisher.Mono;
import java.util.UUID;

@FunctionalInterface
public interface ValidateTokenUseCase {
    Mono<Boolean> validateToken(String token, UUID userId);
}