package com.tms.appconductor.shared.infrastructure.exception;

import com.tms.appconductor.security.domain.exception.CredencialesInvalidasException;
import com.tms.appconductor.security.domain.exception.CuentaBloqueadaException;
import com.tms.appconductor.security.domain.exception.UsuarioNoEncontradoException;
import com.tms.appconductor.shared.application.wrapper.ErrorCode;
import com.tms.appconductor.shared.application.wrapper.ResponseWrapper;
import com.tms.appconductor.shared.domain.exception.BusinessException;
import com.tms.appconductor.shared.domain.exception.EntityNotFoundException;
import com.tms.appconductor.shared.domain.exception.ValidationException;

import jakarta.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
        // ============================================
        // Errores de Autenticación
        // ============================================
        @ExceptionHandler(BadCredentialsException.class)
        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        public Mono<ResponseWrapper<Void>> handleBadCredentials(BadCredentialsException ex) {
                log.warn("Intento de autenticación fallido: {}", ex.getMessage());
                return Mono.just(ResponseWrapper.error(
                                ErrorCode.AUTH_INVALID_CREDENTIALS.getDefaultMessage(),
                                ErrorCode.AUTH_INVALID_CREDENTIALS.getCode()));
        }

        @ExceptionHandler(LockedException.class)
        @ResponseStatus(HttpStatus.LOCKED)
        public Mono<ResponseWrapper<Void>> handleLocked(LockedException ex) {
                log.warn("Cuenta bloqueada: {}", ex.getMessage());
                return Mono.just(ResponseWrapper.error(
                                ErrorCode.AUTH_ACCOUNT_LOCKED.getDefaultMessage(),
                                ErrorCode.AUTH_ACCOUNT_LOCKED.getCode()));
        }

        @ExceptionHandler(AccessDeniedException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        public Mono<ResponseWrapper<Void>> handleAccessDenied(AccessDeniedException ex) {
                log.warn("Acceso denegado: {}", ex.getMessage());
                return Mono.just(ResponseWrapper.error(
                                ErrorCode.AUTH_INSUFFICIENT_PERMISSIONS.getDefaultMessage(),
                                ErrorCode.AUTH_INSUFFICIENT_PERMISSIONS.getCode()));
        }

        @ExceptionHandler(CredencialesInvalidasException.class)
        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        public Mono<ResponseWrapper<Void>> handleCredencialesInvalidas(CredencialesInvalidasException ex) {
                log.warn("Intento de autenticación fallido: {}", ex.getMessage());
                return Mono.just(ResponseWrapper.error(
                                ErrorCode.AUTH_INVALID_CREDENTIALS.getDefaultMessage(),
                                ErrorCode.AUTH_INVALID_CREDENTIALS.getCode()));
        }

        @ExceptionHandler(CuentaBloqueadaException.class)
        @ResponseStatus(HttpStatus.LOCKED)
        public Mono<ResponseWrapper<Void>> handleCuentaBloqueada(CuentaBloqueadaException ex) {
                log.warn("Cuenta bloqueada: {}", ex.getMessage());
                return Mono.just(ResponseWrapper.error(
                                ErrorCode.AUTH_ACCOUNT_LOCKED.getDefaultMessage(),
                                ErrorCode.AUTH_ACCOUNT_LOCKED.getCode()));
        }

        @ExceptionHandler(UsuarioNoEncontradoException.class)
        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        public Mono<ResponseWrapper<Void>> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
                log.warn("Intento de autenticación con usuario inexistente: {}", ex.getMessage());
                return Mono.just(ResponseWrapper.error(
                                ErrorCode.AUTH_INVALID_CREDENTIALS.getDefaultMessage(),
                                ErrorCode.AUTH_INVALID_CREDENTIALS.getCode()));
        }

        // ============================================
        // Errores de Validación
        // ============================================
        @ExceptionHandler(WebExchangeBindException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Mono<ResponseWrapper<Void>> handleValidation(WebExchangeBindException ex) {
                List<String> errors = ex.getFieldErrors().stream()
                                .map(error -> String.format("'%s': %s", error.getField(), error.getDefaultMessage()))
                                .collect(Collectors.toList());
                log.debug("Error de validación: {}", errors);
                return Mono.just(ResponseWrapper.error(
                                ErrorCode.VAL_INVALID_INPUT.getDefaultMessage(),
                                errors));

        }

        @ExceptionHandler(ConstraintViolationException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Mono<ResponseWrapper<Void>> handleConstraintViolation(ConstraintViolationException ex) {
                List<String> errors = ex.getConstraintViolations().stream()
                                .map(v -> String.format("'%s': %s", v.getPropertyPath(), v.getMessage()))
                                .collect(Collectors.toList());
                return Mono.just(ResponseWrapper.error(
                                ErrorCode.VAL_CONSTRAINT_VIOLATION.getDefaultMessage(),
                                errors));
        }

        @ExceptionHandler(ServerWebInputException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Mono<ResponseWrapper<Void>> handleInputException(ServerWebInputException ex) {
                return Mono.just(ResponseWrapper.error(
                                ErrorCode.VAL_INVALID_INPUT.getDefaultMessage(),
                                ex.getReason()));
        }

        // ============================================
        // Errores de Dominio
        // ============================================
        @ExceptionHandler(ValidationException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Mono<ResponseWrapper<Void>> handleDomainValidation(ValidationException ex) {
                return Mono.just(ResponseWrapper.error(
                                ex.getMessage(),
                                ex.getValidationErrors()));
        }

        @ExceptionHandler(EntityNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public Mono<ResponseWrapper<Void>> handleEntityNotFound(EntityNotFoundException ex) {
                log.debug("Entidad no encontrada: {}", ex.getErrorDetail());
                return Mono.just(ResponseWrapper.error(
                                ErrorCode.BIZ_ENTITY_NOT_FOUND.getDefaultMessage(),
                                ex.getErrorDetail()));
        }

        @ExceptionHandler(BusinessException.class)
        @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
        public Mono<ResponseWrapper<Void>> handleBusiness(BusinessException ex) {
                log.warn("Error de negocio: {}", ex.getErrorDetail());
                return Mono.just(ResponseWrapper.error(
                                ex.getMessage(),
                                ex.getErrorDetail()));
        }

        // ============================================
        // Error Genérico
        // ============================================
        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public Mono<ResponseWrapper<Void>> handleGeneric(Exception ex) {
                log.error("Error interno no esperado: {}", ex.getMessage(), ex);
                return Mono.just(ResponseWrapper.error(
                                ErrorCode.TEC_INTERNAL_ERROR.getDefaultMessage(),
                                "Se ha producido un error interno. Por favor, contacte al administrador."));
        }
}