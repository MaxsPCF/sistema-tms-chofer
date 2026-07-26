package com.tms.appconductor.shared.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
        @Value("${server.port:7136}")
        private int serverPort;

        @Bean
        public OpenAPI apiConductorTransporteOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("API Conductor Transporte")
                                                .description("""
                                                                API REST para el Sistema de Gestión de Flota de Transporte.
                                                                ## 🏗️ Arquitectura
                                                                - **Hexagonal** (Ports & Adapters) con separación limpia de capas
                                                                - **DDD** (Domain-Driven Design) con Bounded Contexts
                                                                - **CQRS** (Command Query Responsibility Segregation)
                                                                - **Event-Driven** con Eventos de Dominio

                                                                ## 📦 Módulos
                                                                - **Seguridad**: Autenticación JWT, RBAC, Menús dinámicos tipo Composite
                                                                - **Operaciones**: Órdenes de Trabajo, Viajes, Tracking GPS en tiempo real

                                                                ## ✨ Características
                                                                - ✅ Totalmente reactivo (Spring WebFlux + R2DBC + Netty)
                                                                - ✅ Compatible con .NET (BCrypt Enhanced Hashing SHA-384)
                                                                - ✅ Tracking GPS con soporte offline/online
                                                                - ✅ Rate Limiting y protección OWASP Top 10
                                                                - ✅ Respuesta universal estandarizada
                                                                - ✅ Paginación y ordenamiento unificados

                                                                ## 🔐 Seguridad
                                                                - JWT con refresh tokens
                                                                - RBAC con permisos granulares (Ver, Crear, Editar, Eliminar, Aprobar, Exportar)
                                                                - Bloqueo de cuenta por intentos fallidos
                                                                - Políticas de contraseñas configurables
                                                                """)
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .name("TMS-Transporte Dev Team")
                                                                .email("dev@tms.com")
                                                                .url("https://tms.com"))

                                                .license(new License()
                                                                .name("Propietario - Todos los derechos reservados")
                                                                .url("https://tms.com/license")))

                                .servers(List.of(
                                                new Server()
                                                                .url("http://localhost:" + serverPort)
                                                                .description("Desarrollo Local"),
                                                new Server()
                                                                .url("https://dev-api.tms.com")
                                                                .description("Desarrollo"),
                                                new Server()
                                                                .url("https://staging-api.tms.com")
                                                                .description("Staging"),
                                                new Server()
                                                                .url("https://api.tms.com")
                                                                .description("Producción")))
                                .tags(List.of(
                                                new Tag().name("Autenticación")
                                                                .description("Login, JWT, gestión de sesiones y tokens"),
                                                new Tag().name("Usuarios")
                                                                .description("Gestión de usuarios, perfiles y roles"),
                                                new Tag().name("Viajes")
                                                                .description("Operaciones de viaje para conductores (inicio, fin, tracking)"),
                                                new Tag().name("Tracking")
                                                                .description("Tracking GPS en tiempo real con soporte offline"),
                                                new Tag().name("Órdenes de Trabajo")
                                                                .description("Gestión de órdenes de trabajo y asignaciones"),
                                                new Tag().name("Salud")
                                                                .description("Health checks y monitoreo del sistema")))
                                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                                .components(new Components()
                                                .addSecuritySchemes("Bearer Authentication", createSecurityScheme()));
        }

        private SecurityScheme createSecurityScheme() {
                return new SecurityScheme()
                                .name("Bearer Authentication")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("""
                                                Ingrese el token JWT obtenido en el endpoint de login.
                                                Formato: `Bearer {token}`
                                                El token se obtiene en `POST /api/v1/auth/login`
                                                **Roles disponibles:**
                                                - Conductor
                                                - Cliente (Portal Externo)
                                                - Administrador
                                                """);
        }

}