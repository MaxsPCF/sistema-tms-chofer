package com.genesis.flota.shared.infrastructure.web;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class CorrelationIdFilter implements WebFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CORRELATION_ID_KEY = "correlationId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final String correlationId = extractOrGenerateCorrelationId(exchange);

        // Agregar a headers de respuesta
        exchange.getResponse().getHeaders()
                .set(CORRELATION_ID_HEADER, correlationId);

        return chain.filter(exchange)
                .contextWrite(Context.of(CORRELATION_ID_KEY, correlationId))
                .doOnSubscribe(subscription -> {
                    // Agregar a MDC al inicio
                    MDC.put(CORRELATION_ID_KEY, correlationId);
                    log.debug("Request iniciada: {} {}",
                            exchange.getRequest().getMethod(),
                            exchange.getRequest().getPath());
                })
                .doFinally(signalType -> {
                    // Limpiar MDC al finalizar
                    MDC.remove(CORRELATION_ID_KEY);
                });
    }

    private String extractOrGenerateCorrelationId(ServerWebExchange exchange) {
        return Optional.ofNullable(
                        exchange.getRequest().getHeaders().getFirst(CORRELATION_ID_HEADER))
                .filter(id -> !id.isEmpty())
                .orElseGet(() -> UUID.randomUUID().toString());
    }
}