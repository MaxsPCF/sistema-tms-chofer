package com.genesis.flota.security.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para solicitud de refresco de token JWT")
public record RefreshTokenRequest(

        @Schema(
                description = "Token JWT a refrescar",
                example = "eyJhbGciOiJIUzI1NiJ9...",
                required = true
        )
        @NotBlank(message = "El token es requerido")
        String token,

        @Schema(
                description = "Refresh token para obtener nuevo JWT",
                example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...",
                required = true
        )
        @NotBlank(message = "El refresh token es requerido")
        String refreshToken
) {}