# customer-service

Microservicio de gestión de clientes del sistema bancario. Puerto: **8081**.

## Arquitectura

```
customer-service/
├── customer-domain/         # Customer, Person — POJOs sin dependencias de framework
├── customer-dto/            # CustomerDto, PersonDto — contratos con validaciones
├── customer-application/    # CustomerService — lógica de negocio
├── customer-infrastructure/ # CustomerEntity, PersonEntity, repositorios JPA
├── customer-api/            # Controller REST, GlobalExceptionHandler, OpenAPI config
└── customer-client/         # CustomerClient (WebClient) para consumo por account-service
```

`Customer` hereda de `Person` (relación `@OneToOne` en la capa de infraestructura). El dominio las mantiene como clases independientes para evitar acoplamiento con JPA.

## Ejecución local

```bash
# Desde el directorio raíz: levantar la BD
docker-compose up -d bankingdb

# Desde este directorio
./mvnw clean install -DskipTests
cd customer-api

# Windows
../mvnw.cmd spring-boot:run

# Linux / Mac
../mvnw spring-boot:run
```

Health check: `http://localhost:8081/actuator/health`

## API

Swagger UI: `http://localhost:8081/swagger-ui.html`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/v1/customers` | Crear cliente |
| GET | `/api/v1/customers` | Listar clientes |
| GET | `/api/v1/customers/{customerId}` | Obtener por ID |
| PUT | `/api/v1/customers/{customerId}` | Actualizar cliente |
| DELETE | `/api/v1/customers/{customerId}` | Eliminar cliente |
| GET | `/api/v1/customers/exists/{customerId}` | Verificar existencia (usado por account-service) |

## Tests

```bash
# Todos los tests
./mvnw test

# Prueba unitaria (CustomerService — customer-application)
./mvnw test -pl customer-application -Dtest=CustomerServiceTest

# Windows
.\mvnw.cmd test -pl customer-application -Dtest=CustomerServiceTest
```
