package com.genesis.flota.security.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para solicitud de validación de token JWT")
public record TokenValidationRequest(

        @Schema(
                description = "Token JWT a validar",
                example = "eyJhbGciOiJIUzI1NiJ9...",
                required = true
        )
        @NotBlank(message = "El token es requerido")
        String token,

        @Schema(
                description = "ID del usuario para verificar propiedad del token",
                example = "f4105e29-d9ec-4ef9-8931-459c318f753f"
        )
        String userId
) {}