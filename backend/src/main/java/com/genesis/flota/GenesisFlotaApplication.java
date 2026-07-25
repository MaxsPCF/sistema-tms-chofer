package com.genesis.flota;

import com.genesis.flota.shared.infrastructure.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@EnableWebFlux
@EnableConfigurationProperties(AppProperties.class)
public class GenesisFlotaApplication {

    public static void main(String[] args) {
        // Habilitar debugging de Reactor solo en desarrollo
        if (isDevelopmentProfile()) {
            Hooks.onOperatorDebug();
            Hooks.enableAutomaticContextPropagation();
        }

        SpringApplication.run(GenesisFlotaApplication.class, args);
    }

    /**
     * Verifica si estamos en perfil de desarrollo.
     * En producción, estas herramientas de debugging
     * tienen un costo de rendimiento significativo.
     */
    private static boolean isDevelopmentProfile() {
        String profile = System.getProperty("spring.profiles.active",
                System.getenv("SPRING_PROFILES_ACTIVE"));
        return profile == null ||
                profile.isEmpty() ||
                profile.contains("dev") ||
                profile.contains("local");
    }
}