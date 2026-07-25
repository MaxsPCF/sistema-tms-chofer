package com.genesis.flota.shared.infrastructure.security.filter;

import com.genesis.flota.shared.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtro de autenticación JWT para WebFlux.
 *
 * Intercepta todas las peticiones HTTP y verifica
 * si contienen un token JWT válido en el header Authorization.
 *
 * Si el token es válido, establece el contexto de seguridad
 * con los roles y permisos del usuario.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // No filtrar rutas públicas
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        // Extraer token del header
        String token = extractToken(request);

        if (token == null || token.isEmpty()) {
            log.debug("No se encontró token JWT en la petición a: {}", path);
            return chain.filter(exchange);
        }

        // Validar token
        if (!jwtTokenProvider.validateToken(token)) {
            log.warn("Token JWT inválido o expirado para: {}", path);
            return chain.filter(exchange);
        }

        try {
            // Extraer información del token
            String username = jwtTokenProvider.getUsernameFromToken(token);
            List<String> roles = jwtTokenProvider.getRolesFromToken(token);

            // Crear autoridades
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());

            // Crear autenticación
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            token,
                            authorities
                    );

            log.debug("Usuario autenticado: {} con roles: {}", username, roles);

            // Establecer contexto de seguridad reactivo
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder
                            .withAuthentication(authentication));

        } catch (Exception e) {
            log.error("Error al procesar token JWT: {}", e.getMessage());
            return chain.filter(exchange);
        }
    }

    /**
     * Extrae el token JWT del header Authorization
     */
    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    /**
     * Verifica si la ruta es pública (no requiere autenticación)
     */
    private boolean isPublicPath(String path) {
        return path.contains("/api/v1/auth/login") ||
                path.contains("/api/v1/auth/refresh") ||
                path.contains("/swagger-ui") ||
                path.contains("/v3/api-docs") ||
                path.contains("/webjars") ||
                path.contains("/actuator/health") ||
                path.contains("/api/v1/health");
    }
}