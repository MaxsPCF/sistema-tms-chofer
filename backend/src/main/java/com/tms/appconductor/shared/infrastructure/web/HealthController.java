package com.tms.appconductor.shared.infrastructure.web;

import com.tms.appconductor.shared.application.wrapper.ResponseWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.Map;

@RestController
@Tag(name = "Salud", description = "Endpoints de health check y monitoreo")
public class HealthController {
    private final LocalDateTime startTime = LocalDateTime.now();

    @Value("${spring.application.name:api-conductor-transporte}")
    private String applicationName;

    @Value("${java.version}")
    private String javaVersion;

    @GetMapping("/api/v1/health")
    @Operation(summary = "Verificar estado del servicio")

    public Mono<ResponseWrapper<Map<String, Object>>> health() {
        Duration uptime = Duration.between(startTime, LocalDateTime.now());
        Map<String, Object> healthData = Map.of(
                "status", "UP",
                "service", applicationName,
                "version", "1.0.0",
                "javaVersion", javaVersion,
                "startTime", startTime.toString(),
                "uptime", formatDuration(uptime),
                "timestamp", LocalDateTime.now().toString());

        return Mono.just(ResponseWrapper.success(healthData, "Servicio operativo"));
    }

    @GetMapping("/api/v1/health/detailed")
    @Operation(summary = "Health check detallado")
    public Mono<ResponseWrapper<Map<String, Object>>> detailedHealth() {
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> detailedData = Map.of(
                "status", "UP",
                "service", applicationName,
                "version", "1.0.0",
                "javaVersion", javaVersion,
                "availableProcessors", runtime.availableProcessors(),
                "freeMemory", formatBytes(runtime.freeMemory()),
                "totalMemory", formatBytes(runtime.totalMemory()),
                "maxMemory", formatBytes(runtime.maxMemory()),
                "uptime", formatDuration(Duration.between(startTime, LocalDateTime.now())),
                "timestamp", LocalDateTime.now().toString());

        return Mono.just(ResponseWrapper.success(detailedData, "Estado detallado del servicio"));
    }

    private String formatDuration(Duration duration) {
        return String.format("%dd %dh %dm %ds",
                duration.toDays(),
                duration.toHoursPart(),
                duration.toMinutesPart(),
                duration.toSecondsPart());
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024)
            return bytes + " B";

        if (bytes < 1024 * 1024)
            return String.format("%.2f KB", bytes / 1024.0);

        if (bytes < 1024 * 1024 * 1024)
            return String.format("%.2f MB", bytes / (1024.0 * 1024));

        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

}

/*
 * 
 * @RestController
 * 
 * public class HealthController {
 * 
 * 
 * 
 * @GetMapping("/api/v1/health")
 * 
 * public Mono<Map<String, Object>> health() {
 * 
 * return Mono.just(Map.of(
 * 
 * "status", "UP",
 * 
 * "service", "api-conductor-transporte",
 * 
 * "timestamp", LocalDateTime.now().toString()
 * 
 * ));
 * 
 * }
 * 
 * }
 */
