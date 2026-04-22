# GlowShop

Beauty and wellness e-commerce demo built with **Spring Boot 3**, **Thymeleaf**, **Spring Security**, and **MySQL**. Includes a **REST API** documented with **Springdoc OpenAPI (Swagger UI)**.

## Requirements

- **JDK 17**
- **Maven** (or use the included `mvnw` / `mvnw.cmd`)
- **MySQL** (e.g. XAMPP on port `3306`) — create database `glowshop_db` or rely on `createDatabaseIfNotExist` in the JDBC URL
- **Kafka** is **not** required to run the app locally (configuration is present for optional messaging; the app starts without a broker)

## Configuration

Edit `src/main/resources/application.properties` if your MySQL credentials differ:

| Property | Typical local (XAMPP) |
|----------|------------------------|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/glowshop_db?...` |
| `spring.datasource.username` | `root` |
| `spring.datasource.password` | *(empty)* |
| `jwt.secret` | Strong secret (see `application.properties`); override in production |
| `jwt.expiration-ms` | Access token lifetime (default 24h) |

Default server port: **8080**.

## Run

```bash
./mvnw.cmd spring-boot:run
```

(On Linux/macOS: `./mvnw spring-boot:run`.)

Open **http://localhost:8080**

### Seeded admin (from `DataInitializer`)

- **Email:** `admin@glowshop.com`  
- **Password:** `admin123`

### Useful URLs

| Resource | URL |
|----------|-----|
| Storefront | http://localhost:8080/products |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api-docs |

REST catalog **GET** endpoints are public. **POST / PUT / DELETE** on `/api/products` and `/api/categories` require **ADMIN**, satisfied by either:

1. **Session:** log in via the web UI, then use **Try it out** in Swagger from the same browser, or  
2. **JWT:** `POST /api/auth/login` with `{"email":"…","password":"…"}`, copy `accessToken`, then in Swagger click **Authorize** and send `Authorization: Bearer <token>` on mutating requests.

## Tests

Unit tests use **H2** in-memory (`src/test/resources/application-test.properties`). They do **not** require MySQL.

```bash
./mvnw.cmd test
```

## REST API overview

| Method | Path | Notes |
|--------|------|--------|
| POST | `/api/auth/login` | JSON body `email`, `password` → JWT (`accessToken`, `tokenType`, `expiresIn`) |
| GET | `/api/products` | Paged list; optional `search`, `categoryId`, `page`, `size`, `sort` |
| GET | `/api/products/{id}` | Product detail |
| POST | `/api/products` | ADMIN — JSON body (see Swagger schema) |
| PUT | `/api/products/{id}` | ADMIN |
| DELETE | `/api/products/{id}` | ADMIN |
| GET | `/api/categories` | Paged categories |
| GET | `/api/categories/{id}` | Category detail |
| POST / PUT / DELETE | `/api/categories` … | ADMIN |

## Project layout

- `src/main/java/com/ecommerce/glowshop` — application code (`controller`, `service`, `api` REST controllers, `security`, etc.)
- `src/main/resources/templates` — Thymeleaf views