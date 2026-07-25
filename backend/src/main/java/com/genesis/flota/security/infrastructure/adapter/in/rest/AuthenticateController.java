package com.genesis.flota.security.infrastructure.adapter.in.rest;

import com.genesis.flota.security.application.command.LoginCommand;
import com.genesis.flota.security.application.dto.AuthResponse;
import com.genesis.flota.security.application.port.in.AuthenticateUserUseCase;
import com.genesis.flota.security.infrastructure.adapter.in.rest.dto.LoginRequest;
import com.genesis.flota.shared.application.wrapper.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/authenticate")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints de autenticación y autorización")
public class AuthenticateController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario y retorna JWT + menú dinámico basado en roles"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login exitoso",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas"
            ),
            @ApiResponse(
                    responseCode = "423",
                    description = "Cuenta bloqueada"
            )
    })
    public Mono<ResponseWrapper<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        log.info("Solicitud de login para usuario: {}", request.email());

        LoginCommand command = new LoginCommand(
                request.email(),
                request.password()
        );

        return authenticateUserUseCase.authenticate(command)
                .map(authResponse -> ResponseWrapper.success(
                        authResponse,
                        "Login exitoso."))
                .doOnSuccess(response ->
                        log.info("Login exitoso para: {}", request.email()))
                .doOnError(error ->
                        log.warn("Login fallido para: {} - {}",
                                request.email(), error.getMessage()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refrescar token JWT")
    public Mono<ResponseWrapper<String>> refreshToken(
            @RequestHeader("Authorization") String bearerToken) {
        // Implementar refresh token
        return Mono.just(ResponseWrapper.success("Token refreshed", "Token actualizado"));
    }

    @PostMapping("/validate")
    @Operation(summary = "Validar token JWT")
    public Mono<ResponseWrapper<Boolean>> validateToken(
            @RequestHeader("Authorization") String bearerToken) {
        // Implementar validación
        return Mono.just(ResponseWrapper.success(true, "Token válido"));
    }
}