# GlowShop

Beauty and wellness e-commerce demo: Spring Boot 3, Thymeleaf, MySQL, optional Kafka, OpenAPI/Swagger.

## Prerequisites

- Java 17
- Maven (or use `./mvnw` / `mvnw.cmd` in this folder)
- MySQL (e.g. XAMPP on port 3306)

## Database

Create an empty schema or rely on `createDatabaseIfNotExist` in the JDBC URL.

Default in `application.properties`:

- URL: `jdbc:mysql://localhost:3306/glowshop_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC`
- User: `root`
- Password: empty (XAMPP default)

Adjust if your MySQL differs.

## Run (no Kafka)

```bash
./mvnw spring-boot:run
```

Windows: `mvnw.cmd spring-boot:run`

- App: http://localhost:8080  
- Swagger UI: http://localhost:8080/swagger-ui.html  
- OpenAPI JSON: http://localhost:8080/api-docs  

Seeded admin: `admin@glowshop.com` / `admin123`

## Run with Kafka

1. Start a Kafka broker on `localhost:9092` (and ZooKeeper/KRaft as required by your install).
2. Create the topic (or rely on broker auto-create if enabled):

   `order.placed`

3. Start the app:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=kafka"
```

On each successful checkout, a JSON message is sent to `order.placed`. A sample consumer logs incoming messages.

## Project layout (high level)

- `controller` — MVC (storefront, admin, cart, orders)
- `api` — JSON REST + DTOs
- `service` — business logic
- `kafka` — optional order event publisher/listener (`kafka` profile)
- `templates` — Thymeleaf views

## Tests

```bash
./mvnw test
```

(Expand with unit tests for services as needed for coursework.)
