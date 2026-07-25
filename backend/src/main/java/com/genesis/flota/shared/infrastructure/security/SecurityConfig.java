package com.genesis.flota.shared.infrastructure.security;

import com.genesis.flota.shared.infrastructure.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.security.web.server.header.XXssProtectionServerHttpHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // Deshabilitar características no necesarias para API REST
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)

                // Stateless: sin sesiones en servidor
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

                // Agregar filtro JWT
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)

                // Configurar CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                /*// Configurar autorización por rutas
                .authorizeExchange(exchanges -> exchanges
                        // Rutas públicas
                        .pathMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/auth/refresh").permitAll()

                        // Swagger/OpenAPI
                        .pathMatchers("/swagger-ui.html", "/swagger-ui/**",
                                "/v3/api-docs/**", "/webjars/**").permitAll()

                        // Actuator
                        .pathMatchers("/actuator/health/**").permitAll()
                        .pathMatchers("/actuator/info").permitAll()
                        .pathMatchers("/actuator/**").hasRole("ADMIN")

                        // Health check
                        .pathMatchers("/api/v1/health").permitAll()

                        // Administración
                        .pathMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        // Conductores
                        .pathMatchers("/api/v1/trips/**").hasRole("CONDUCTOR")
                        .pathMatchers("/api/v1/tracking/**").hasRole("CONDUCTOR")

                        // Clientes
                        .pathMatchers("/api/v1/orders/**").hasAnyRole("CLIENTE", "ADMIN")

                        // El resto requiere autenticación
                        .anyExchange().authenticated()
                )*/
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                )


                // Cabeceras de seguridad OWASP
                .headers(headers -> headers
                        // Content Security Policy
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; script-src 'self'; style-src 'self'"))

                        // X-Frame-Options: DENY
                        .frameOptions(frame -> frame
                                .mode(XFrameOptionsServerHttpHeadersWriter.Mode.DENY))

                        // X-XSS-Protection
                        .xssProtection(xss -> xss
                                .headerValue(XXssProtectionServerHttpHeadersWriter.HeaderValue.ENABLED))

                        // Cache-Control
                        .cache(cache -> cache.disable())

                        // HTTP Strict Transport Security (HSTS)
                        .hsts(hsts -> hsts
                                .includeSubdomains(true)
                                .maxAge(Duration.ofSeconds(31536000L))  // Usar Duration, no long
                                .preload(true))
                )

                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://localhost:3000",
                "https://app.genesis.com"
        ));
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of(
                "Authorization", "X-Request-ID", "X-Correlation-ID", "X-Trace-ID"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}