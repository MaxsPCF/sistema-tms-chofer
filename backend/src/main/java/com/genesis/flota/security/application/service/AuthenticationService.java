package com.genesis.flota.security.application.service;

import com.genesis.flota.security.application.command.LoginCommand;
import com.genesis.flota.security.application.dto.AuthResponse;
import com.genesis.flota.security.application.dto.MenuTreeDto;
import com.genesis.flota.security.application.dto.UsuarioDto;
import com.genesis.flota.security.application.port.in.AuthenticateUserUseCase;
import com.genesis.flota.security.application.port.out.PasswordEncoderPort;
import com.genesis.flota.security.domain.exception.CredencialesInvalidasException;
import com.genesis.flota.security.domain.exception.CuentaBloqueadaException;
import com.genesis.flota.security.domain.exception.UsuarioNoEncontradoException;
import com.genesis.flota.security.domain.model.Rol;
import com.genesis.flota.security.domain.model.Usuario;
import com.genesis.flota.security.domain.repository.ModuloRepository;
import com.genesis.flota.security.domain.repository.UsuarioRepository;
import com.genesis.flota.security.domain.service.MenuBuilderService;
import com.genesis.flota.shared.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticateUserUseCase {

    private final UsuarioRepository usuarioRepository;
    private final ModuloRepository moduloRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MenuBuilderService menuBuilderService;

    @Value("${app.security.max-failed-attempts:5}")
    private int maxFailedAttempts;

    // Constantes basadas en CÓDIGO (no en ID)
    private static final String ROL_REQUERIDO = "Conductor (Portal Externo)";
    private static final String CODIGO_APLICACION = "APP_CONDUCTOR";

    @Override
    @Transactional
    public Mono<AuthResponse> authenticate(LoginCommand command) {
        log.debug("Intentando autenticar: {}", command.email());

        return usuarioRepository.findByEmailWithRoles(command.email())
                .switchIfEmpty(Mono.error(new UsuarioNoEncontradoException(command.email())))
                .flatMap(this::validarEstadoUsuario)
                .flatMap(this::validarRolConductor)
                .flatMap(usuario -> verificarPassword(usuario, command.password()));
    }

    private Mono<Usuario> validarEstadoUsuario(Usuario usuario) {
        if (usuario.estaBloqueado()) {
            return Mono.error(new CuentaBloqueadaException(usuario.getEmail()));
        }
        if (!usuario.estaActivo()) {
            return Mono.error(new CredencialesInvalidasException("Cuenta desactivada"));
        }
        return Mono.just(usuario);
    }

    /**
     * Valida que el usuario tenga el rol "Conductor (Portal Externo)"
     * para la aplicación "APP_CONDUCTOR".
     */
    private Mono<Usuario> validarRolConductor(Usuario usuario) {
        boolean tieneRolConductor = usuario.getRoles().stream()
                .filter(Rol::isActivo)
                .anyMatch(rol -> ROL_REQUERIDO.equals(rol.getNombreRol())
                        && CODIGO_APLICACION.equals(rol.getCodigoAplicacion()));

        if (!tieneRolConductor) {
            log.warn("Usuario '{}' sin rol '{}' para app '{}'",
                    usuario.getEmail(), ROL_REQUERIDO, CODIGO_APLICACION);
            return Mono.error(new CredencialesInvalidasException(
                    "No tiene permisos para acceder a esta aplicación"));
        }

        log.info("Usuario '{}' autorizado como '{}'", usuario.getEmail(), ROL_REQUERIDO);
        return Mono.just(usuario);
    }

    private Mono<AuthResponse> verificarPassword(Usuario usuario, String password) {
        return passwordEncoder.matchesReactive(password, usuario.getPasswordHash())
                .flatMap(matches -> matches
                        ? manejarLoginExitoso(usuario)
                        : manejarLoginFallido(usuario));
    }

    private Mono<AuthResponse> manejarLoginExitoso(Usuario usuario) {
        log.info("Login exitoso: {}", usuario.getNombreUsuario());
        usuario.registrarAccesoExitoso();
        return usuarioRepository.save(usuario)
                .then(buildAuthResponse(usuario));
    }

    private Mono<AuthResponse> manejarLoginFallido(Usuario usuario) {
        usuario.incrementarIntentosFallidos();
        if (usuario.getIntentosFallidos() >= maxFailedAttempts) {
            usuario.bloquear();
            log.warn("Usuario bloqueado: {}", usuario.getNombreUsuario());
        }
        return usuarioRepository.save(usuario)
                .then(Mono.error(new CredencialesInvalidasException()));
    }

    private Mono<AuthResponse> buildAuthResponse(Usuario usuario) {
        String token = jwtTokenProvider.generateToken(usuario);
        LocalDateTime expiration = jwtTokenProvider.getExpirationDate(token);

        // Roles activos para el JWT
        List<String> rolesActivos = usuario.getRoles().stream()
                .filter(Rol::isActivo)
                .map(Rol::getNombreRol)
                .toList();

        // Menús filtrados por código de aplicación "APP_CONDUCTOR"
        return moduloRepository.findMenusByUsuarioIdAndAplicacionCodigo(
                        usuario.getIdUsuario(), CODIGO_APLICACION)
                .collectList()
                .map(menuBuilderService::construirArbolMenus)
                .map(modulos -> {
                    List<MenuTreeDto> menus = modulos.stream()
                            .map(MenuTreeDto::from)
                            .toList();
                    return new AuthResponse(token, expiration,
                            new UsuarioDto(usuario.getIdUsuario(), usuario.getNombreUsuario(),
                                    usuario.getEmail(), rolesActivos),
                            menus);
                });
    }
}