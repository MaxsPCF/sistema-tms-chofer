package com.tms.appconductor.security.application.dto;

import java.util.List;
import java.util.UUID;

public record UsuarioDto(
                UUID idUsuario,
                String nombreUsuario,
                String email,
                List<String> roles) {
}