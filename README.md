# E-Commerce Backend

A modular e-commerce backend built with **Java 21, Spring Boot, PostgreSQL, and Redis**, focusing on scalability, reliability, concurrency control, and clean software architecture.

The project implements real-world backend engineering patterns, including **Domain-Driven Design (DDD), atomic inventory management, Redis caching, API idempotency, transactional outbox, and Saga-based payment processing**.

## Tech Stack

| Category | Technologies |
|---|---|
| Backend | Java 21, Spring Boot 3.4 |
| Database | PostgreSQL 16, Spring Data JPA |
| Caching | Redis 7, Spring Cache |
| Migrations | Flyway |
| Resilience | Resilience4j |
| Monitoring | Spring Boot Actuator, Micrometer, SLF4J MDC |
| DevOps | Docker, Docker Compose, Maven |

## Architecture

The application follows a **modular monolith architecture** inspired by Domain-Driven Design. Business functionality is organized into independent modules with clear responsibilities.

```text
src/main/java/com/example/shop_app/
├── catalog/         # Products, pricing, and caching
├── inventory/       # Stock management and reservations
├── order/           # Checkout and order lifecycle
├── payment/         # Payment processing and compensation
└── common/          # Shared infrastructure
    ├── config/
    ├── domain/
    ├── filter/
    ├── metrics/
    ├── outbox/
    └── service/
```

Each business module separates API, application logic, domain models, and infrastructure concerns.

## Key Features

### Inventory Management & Concurrency

Prevents overselling through **atomic conditional SQL updates**, ensuring stock reservations remain consistent under concurrent requests.

- Atomic stock reservations at the database level.
- Two-phase inventory lifecycle: `RESERVED → COMMITTED / RELEASED`.
- Automatic stock restoration following payment failures.

### Redis Caching

Implements the **Cache-Aside pattern** to improve product retrieval performance.

- Spring Cache with `@Cacheable` and `@CacheEvict`.
- Configurable cache expiration (TTL).
- JSON serialization with Jackson.

### API Idempotency

Uses Redis-backed request coordination and the `Idempotency-Key` header to handle duplicate checkout and payment requests.

- Prevents duplicate order processing.
- Returns stored responses for completed requests.
- Handles concurrent duplicate requests with `409 Conflict`.

### Transactional Outbox

Ensures business operations and their corresponding events are persisted within the same database transaction.

A scheduled processor publishes pending events asynchronously, supporting reliable **at-least-once event delivery**.

### Payment Processing & Fault Tolerance

Implements Saga-style compensating operations to maintain consistency between payments, orders, and inventory.

- **Payment successful:** Confirm order and commit reserved stock.
- **Payment failed:** Cancel order and release reserved stock.
- **Resilience4j Circuit Breaker:** Protects against downstream payment service failures.

### Observability

- Request tracing using `X-Correlation-Id` and SLF4J MDC.
- Custom business metrics with Micrometer.
- Application health monitoring through Spring Boot Actuator.

## Getting Started

### Prerequisites

- Java 21
- Docker and Docker Compose
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/AyoubGourstane04/shop-app-backend.git
cd shop-app-backend
```

### 2. Configure Environment Variables

The application uses environment variables to manage database configuration and sensitive credentials.

Copy the provided `.env.example` file:

**Linux / macOS**
```bash
cp .env.example .env
```

**Windows PowerShell**
```powershell
Copy-Item .env.example .env
```

Update `.env` with your local configuration using the variable names defined in `.env.example`.

> **Security:** The `.env` file contains private credentials and must never be committed. Only `.env.example`, containing safe placeholder values, should be tracked by Git.

Ensure `.gitignore` contains:

```gitignore
.env
.env.*
!.env.example
```

Environment variables must also be loaded by the configured application startup mechanism.

### 3. Start Infrastructure

```bash
docker compose up -d
```

This starts the configured PostgreSQL and Redis services.

### 4. Run the Application

**Linux / macOS**
```bash
./mvnw spring-boot:run
```

**Windows PowerShell**
```powershell
.\mvnw.cmd spring-boot:run
```

Flyway automatically applies pending database migrations during startup.

**Default API URL:** `http://localhost:8080`

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/products` | Create a product |
| `POST` | `/api/v1/inventory/{id}/initialize` | Initialize product stock |
| `POST` | `/api/v1/orders/checkout` | Create an idempotent order |
| `POST` | `/api/v1/payments/{orderId}` | Process order payment |
| `GET` | `/actuator/health` | Application health check |

Checkout and payment requests support the `Idempotency-Key` header to handle retries safely.

### Order Processing Flow

**Successful payment:**

`Checkout → Reserve Stock → Process Payment → Confirm Order`

**Failed payment:**

`Checkout → Reserve Stock → Payment Failed → Release Stock → Cancel Order`

## Design Principles

- **Domain-Driven Design:** Clear boundaries between business modules.
- **Data Consistency:** ACID transactions and transactional outbox.
- **Concurrency Safety:** Atomic inventory operations.
- **Fault Tolerance:** Circuit Breaker and compensation workflows.
- **Performance:** Redis caching and cache invalidation.
- **Maintainability:** Modular architecture and separation of concerns.
- **Observability:** Correlation IDs, health checks, and business metrics.

## License

This project is licensed under the [MIT License](LICENSE).