# Backend

Spring Boot microservices for the HRMS system.

## Services

| Service | Port | DB | Description |
|---|---|---|---|
| gateway | 8082 | - | API gateway |
| auth-service | 8086 | auth_db (5436) | JWT auth, RBAC |
| employee-service | 8081 | employee_db (5433) | Employee CRUD |
| department-service | 8083 | department_db (5434) | Department CRUD |
| leave-service | 8084 | leave_db (5435) | Leave management |

## Quick Start

```bash
# Start all databases
docker compose up -d employee-pg department-pg leave-pg auth-pg

# Run any service
cd employee-service && mvn spring-boot:run
```

## Tech

- Java 21, Spring Boot 3.4.5, Spring Security, JWT
- PostgreSQL 17, Spring Data JPA
- CQRS pattern per service (Command + Query service split)
- Lombok for boilerplate reduction
