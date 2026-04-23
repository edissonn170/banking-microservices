# account-service

Microservicio de gestión de cuentas bancarias y movimientos. Puerto: **8082**.

Depende de `customer-service` para validar la existencia de clientes al crear cuentas y para construir los reportes de estado de cuenta.

## Arquitectura

```
account-service/
├── account-domain/         # Account, Transaction — POJOs sin dependencias de framework
├── account-dto/            # AccountDto, TransactionDto, AccountStatementDto — contratos con validaciones
├── account-application/    # AccountService, MovementService, ReportService — lógica de negocio
├── account-infrastructure/ # AccountEntity, TransactionEntity, repositorios JPA
├── account-api/            # Controllers REST, GlobalExceptionHandler, OpenAPI config
└── account-client/         # AccountClient (WebClient) para consumo por otros servicios
```

El acceso a BD es sincrónico (JPA), adaptado al pipeline reactivo de WebFlux mediante `Mono.fromCallable(...).subscribeOn(Schedulers.boundedElastic())`.

## Ejecución local

```bash
# Desde el directorio raíz: levantar la BD
docker-compose up -d bankingdb

# Desde este directorio
./mvnw clean install -DskipTests
cd account-api

# Windows
../mvnw.cmd spring-boot:run

# Linux / Mac
../mvnw spring-boot:run
```

> `customer-service` debe estar corriendo en `http://localhost:8081` antes de iniciar este servicio.

Health check: `http://localhost:8082/actuator/health`

## API

Swagger UI: `http://localhost:8082/swagger-ui.html`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/accounts` | Crear cuenta |
| GET | `/api/v1/accounts` | Listar cuentas |
| GET | `/api/v1/accounts/{accountId}` | Obtener por ID |
| GET | `/api/v1/accounts/by-number/{accountNumber}` | Obtener por número |
| GET | `/api/v1/accounts/by-customer/{customerId}` | Cuentas de un cliente |
| PUT | `/api/v1/accounts/{accountId}` | Actualizar cuenta |
| DELETE | `/api/v1/accounts/{accountId}` | Eliminar cuenta |
| POST | `/api/v1/movements` | Registrar movimiento |
| GET | `/api/v1/movements` | Listar movimientos |
| GET | `/api/v1/movements/{movementId}` | Obtener por ID |
| GET | `/api/v1/movements/by-account/{accountId}` | Movimientos de una cuenta |
| PUT | `/api/v1/movements/{movementId}` | Actualizar movimiento |
| DELETE | `/api/v1/movements/{movementId}` | Eliminar movimiento |
| GET | `/api/v1/reports/{clientId}?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` | Estado de cuenta por rango de fechas |

## Tests

```bash
./mvnw test
```
