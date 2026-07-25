# Genesis Flota API 🚛

API REST para el Sistema de Gestión de Flota de Transporte.

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-Proprietary-red.svg)]()

---

## 🏗️ Arquitectura

Arquitectura Hexagonal (Ports & Adapters) + DDD + CQRS

📦 bounded-contexts/
├── 🔐 security/ # Autenticación, JWT, RBAC, Menús
├── 🚛 operations/ # Viajes, Tracking GPS, Órdenes de Trabajo
└── 🧩 shared/ # Kernel compartido (DDD base, utilidades)

### Patrones Implementados

| Patrón                     | Descripción                                               |
| -------------------------- | --------------------------------------------------------- |
| **Hexagonal Architecture** | Puertos y adaptadores para desacoplar infraestructura     |
| **Domain-Driven Design**   | Bounded Contexts, Agregados, Value Objects, Domain Events |
| **CQRS**                   | Separación de comandos (escritura) y consultas (lectura)  |
| **SOLID**                  | Principios aplicados en cada capa                         |
| **OWASP Top 10**           | Mitigación desde el diseño                                |

---

## 🛠️ Stack Tecnológico

| Tecnología            | Versión | Uso                                            |
| --------------------- | ------- | ---------------------------------------------- |
| **Java**              | 25      | Lenguaje principal (records, pattern matching) |
| **Spring Boot**       | 4.0.7   | Framework base                                 |
| **Spring WebFlux**    | 7.0.8   | Stack reactivo (Netty)                         |
| **Spring Security**   | 7.0.6   | Autenticación y autorización                   |
| **Spring Data R2DBC** | 4.0.6   | Acceso reactivo a BD                           |
| **SQL Server**        | 2025    | Base de datos principal                        |
| **JJWT**              | 0.13.0  | JSON Web Tokens                                |
| **BCrypt (favre)**    | 0.10.2  | Hashing compatible con .NET                    |
| **SpringDoc OpenAPI** | 3.0.0   | Documentación Swagger                          |
| **Lombok**            | 1.18.46 | Reducción de boilerplate                       |
| **Bucket4j**          | 8.10.1  | Rate Limiting                                  |
| **Maven**             | 3.9+    | Build tool                                     |

---

## 🔐 Seguridad

### Compatibilidad .NET BCrypt

El password hashing es **100% compatible** con `BCrypt.Net.EnhancedHashPassword` de .NET:
Flujo: Password → SHA-384 → Base64 → BCrypt (workFactor=12)

Esto permite que ambos sistemas (.NET y Java) validen contraseñas contra los mismos hashes.

### JWT Authentication

- Tokens firmados con HMAC-SHA256 (clave 256+ bits)
- Expiración configurable (default: 1 hora)
- Roles incluidos en el payload
- Refresh tokens

### RBAC

- Roles por aplicación
- Permisos granulares: Ver, Crear, Editar, Eliminar, Aprobar, Exportar
- Menús dinámicos tipo Composite (estructura de árbol)

---

## 📦 Estructura del Proyecto

```
backend/
├── src/main/java/com/genesis/flota/
│ ├── GenesisFlotaApplication.java
│ ├── shared/ # Kernel compartido
│ │ ├── domain/
│ │ │ ├── model/ # BaseEntity, AggregateRoot, ValueObject
│ │ │ ├── exception/ # BaseDomainException, BusinessException
│ │ │ └── service/ # DomainService, EventPublisher
│ │ ├── application/
│ │ │ ├── port/ # Input/Output Ports
│ │ │ └── wrapper/ # ResponseWrapper, PagedResponse
│ │ └── infrastructure/
│ │ ├── config/ # R2DBC, OpenAPI, Jackson, WebFlux
│ │ ├── security/ # JWT, SecurityConfig, BCrypt
│ │ ├── exception/ # GlobalExceptionHandler
│ │ └── web/ # HealthController, CorrelationIdFilter
│ ├── security/ # Bounded Context: Seguridad
│ │ ├── domain/
│ │ │ ├── model/ # Usuario, Rol, Modulo, Permiso
│ │ │ ├── service/ # MenuBuilderService, PasswordPolicy
│ │ │ └── repository/ # Output Ports (interfaces)
│ │ ├── application/
│ │ │ ├── port/in/ # AuthenticateUserUseCase, GetUserMenusUseCase
│ │ │ ├── port/out/ # PasswordEncoderPort
│ │ │ ├── command/ # LoginCommand
│ │ │ ├── dto/ # AuthResponse, UsuarioDto, MenuTreeDto
│ │ │ └── service/ # AuthenticationService
│ │ └── infrastructure/
│ │ ├── adapter/in/rest/ # AuthController, LoginRequest
│ │ ├── adapter/out/persistence/ # UsuarioPersistenceAdapter, Entities
│ │ └── adapter/out/security/ # NetCompatibleBCryptPasswordEncoder
│ └── operations/ # Bounded Context: Operaciones
│ └── ... (en desarrollo)
└── src/main/resources/
├── application.yml
└── application-dev.yml
```

---

## 🚀 Quick Start

### Requisitos

- Java 25
- Maven 3.9+
- SQL Server 2025 (o Docker)

### Configuración

1. Clonar el repositorio:

```bash
git clone https://github.com/MaxsPCF/sistema-tms-chofer.git
cd sistema-tms-chofer/backend
```

2. Configurar application-dev.yml:

```yml
spring:
 r2dbc:
  host: Name_Server
  port: 1433
  database: Name_BD
  username: User_BD
  password: Password_User
```

3. Ejecutar:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

4. Probar:

```bash
# Health Check
curl http://localhost:7136/api/v1/health

# Swagger UI
open http://localhost:7136/swagger-ui/index.html

# Login
curl -X POST http://localhost:7136/api/v1/authenticate/login \
  -H "Content-Type: application/json" \
  -d '{"email": "usuario@genesis.com", "password": "123456"}'
```

## SQL Server con Docker

```bash
docker run -d \
  --name sqlserver2025 \
  -e "ACCEPT_EULA=Y" \
  -e "MSSQL_SA_PASSWORD=YourStrong!Passw0rd" \
  -p 1433:1433 \
  mcr.microsoft.com/mssql/server:2025-latest
```

## 📡 Endpoints

| Método | Endpoint                     | Descripción     | Auth |
| ------ | ---------------------------- | --------------- | ---- |
| GET    | /api/v1/health               | Health Check    | ❌   |
| GET    | /actuator/health             | Actuator Health | ❌   |
| POST   | /api/v1/authenticate/login   | Login           | ❌   |
| POST   | /api/v1/authenticate/refresh | Refresh Token   | ❌   |
| GET    | /swagger-ui/index.html       | Swagger UI      | ❌   |
| GET    | /v3/api-docs                 | OpenAPI Docs    | ❌   |

## 🔧 Variables de Entorno

| Variable               | Default               | Descripción       |
| ---------------------- | --------------------- | ----------------- |
| DB_HOST                | localhost             | Host SQL Server   |
| DB_PORT                | 1433                  | Puerto SQL Server |
| DB_NAME                | TransporteCargaPesada | Base de datos     |
| DB_USER                | usr-desarrollo        | Usuario BD        |
| DB_PASS                | 123456                | Password BD       |
| SERVER_PORT            | 7136                  | Puerto de la API  |
| JWT_SECRET             | (interno)             | Clave secreta JWT |
| SPRING_PROFILES_ACTIVE | dev                   | Perfil activo     |

## 📊 Estado del Proyecto

### Módulo Seguridad

| Funcionalidad              | Estado           |
| -------------------------- | ---------------- |
| Login con JWT              | ✅ Completado    |
| Compatibilidad BCrypt .NET | ✅ Completado    |
| RBAC                       | 🚧 En desarrollo |
| Menús dinámicos            | 🚧 En desarrollo |
| Refresh Token              | 🚧 En desarrollo |

### Módulo Operaciones

| Funcionalidad      | Estado       |
| ------------------ | ------------ |
| Órdenes de Trabajo | 🔜 Pendiente |
| Viajes             | 🔜 Pendiente |
| Tracking GPS       | 🔜 Pendiente |
| Soporte Offline    | 🔜 Pendiente |

---

## 📝 Licencia

## Propietario - Todos los derechos reservados © 2026 Genesis

## 👥 Autores

- Maxs Cayetano - Arquitecto de Software Principal
- Genesis Dev Team

---

## 🔗 Enlaces

- Documentación Swagger = http://localhost:7136/swagger-ui/index.html
- Health Check = http://localhost:7136/api/v1/health
- Actuator = http://localhost:7136/actuator
