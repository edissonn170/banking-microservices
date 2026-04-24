# Banking Microservices

Sistema bancario basado en microservicios con Java 17, Spring Boot 3.4 y PostgreSQL.

## Arquitectura

El proyecto aplica **Clean Architecture** con separación de responsabilidades en 6 módulos Maven por servicio:

```
┌─────────────────────────────────────────────────────────────────────┐
│  customer-service (:8081)          account-service (:8082)          │
│                                                                     │
│  ┌─────────────┐                  ┌─────────────┐                   │
│  │  api        │  WebClient HTTP  │  api        │                   │
│  │  application│ ◄──────────────  │  application│                   │
│  │  domain     │   (async/non-    │  domain     │                   │
│  │  infra      │    blocking)     │  infra      │                   │
│  │  dto        │                  │  dto        │                   │
│  │  client     │                  │  client     │                   │
│  └──────┬──────┘                  └──────┬──────┘                   │
│         │                                │                          │
│         └──────────────┬─────────────────┘                          │
│                        ▼                                            │
│               ┌────────────────┐                                    │
│               │  PostgreSQL 15 │                                    │
│               └────────────────┘                                    │
└─────────────────────────────────────────────────────────────────────┘
```

La comunicación entre servicios es **asincrónica no-bloqueante** (Spring WebFlux + WebClient). La persistencia usa JPA sobre un pool de hilos dedicado (`Schedulers.boundedElastic()`), desacoplando el acceso bloqueante a BD del pipeline reactivo.

### Capas por servicio

| Módulo | Responsabilidad|
|--------|----------------|
| `*-domain` | Modelos de dominio (POJOs puros, sin dependencias de framework) |
| `*-dto` | Contratos de API con validaciones Jakarta |
| `*-application` | Servicios, mappers, excepciones de negocio |
| `*-infrastructure` | Entities JPA, implementaciones de repositorio |
| `*-api` | Controllers REST, manejo global de excepciones, OpenAPI |
| `*-client` | WebClient para consumo inter-servicios |

## Stack

| Tecnología | Versión |
|------------|---------|
| Java | 17 |
| Spring Boot | 3.4.0 |
| Spring WebFlux | 3.4.0 |
| Spring Data JPA | 3.4.0 |
| PostgreSQL | 15 |
| Lombok | 1.18.x |
| Docker Compose | 2.x |

## Requisitos

- Docker Desktop con Docker Compose v2

## Ejecución con Docker Compose

```bash
# Levantar todos los servicios (BD + customer-service + account-service)
docker-compose up --build -d

# Ver logs en tiempo real
docker-compose logs -f

# Detener
docker-compose down

# Detener y limpiar volúmenes
docker-compose down -v
```

Los servicios estarán disponibles en:
- customer-service: `http://localhost:8081`
- account-service: `http://localhost:8082`

Documentación interactiva (Swagger UI):
- `http://localhost:8081/swagger-ui.html`
- `http://localhost:8082/swagger-ui.html`

## Ejecución local

Requiere adicionalmente: Java 17 y Maven 3.9+.

```bash
# 1. Levantar solo la base de datos
docker-compose up -d bankingdb

# 2. Compilar e iniciar customer-service (terminal 1)
cd customer-service
./mvnw clean install -DskipTests
cd customer-api && ../mvnw spring-boot:run

# 3. Compilar e iniciar account-service (terminal 2)
cd account-service
./mvnw clean install -DskipTests
cd account-api && ../mvnw spring-boot:run
```

> En Windows usar `mvnw.cmd` en lugar de `mvnw`.

## Tests

```bash
cd account-service && ./mvnw test
cd customer-service && ./mvnw test
```

## Autor

Edison Narváez
