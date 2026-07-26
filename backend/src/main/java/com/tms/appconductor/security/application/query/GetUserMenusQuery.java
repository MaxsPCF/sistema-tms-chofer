package com.tms.appconductor.security.application.query;

import java.util.UUID;

public record GetUserMenusQuery(
                UUID idUsuario,
                Integer idAplicacion) {
}