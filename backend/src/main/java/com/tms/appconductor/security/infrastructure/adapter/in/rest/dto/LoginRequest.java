package com.tms.appconductor.security.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para solicitud de login")
public record LoginRequest(
                @Schema(description = "Email de usuario", example = "user@genesis.com", required = true) @NotBlank(message = "El email de usuario es requerido") String email,

                @Schema(description = "Contraseña del usuario", example = "MiPassword123!", required = true) @NotBlank(message = "La contraseña es requerida") String password) {
}