# HRMS - Human Resource Management System

A microservices-based Human Resource Management System built with **Spring Boot 3.4.5** (Java 21) on the backend and **React 19** with **Vite** on the frontend.

## Architecture

```
humanresourcemanagement/
├── backend/
│   ├── gateway/            # Spring Cloud Gateway (port 8082)
│   ├── auth-service/       # JWT authentication & RBAC (port 8086)
│   ├── employee-service/   # Employee CRUD + search (port 8081)
│   ├── department-service/ # Department management (port 8083)
│   ├── leave-service/      # Leave requests & balances (port 8084)
│   └── docker-compose.yaml
└── frontend/               # React + Vite SPA
```

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3.4.5, Spring Security, JWT (Auth0 java-jwt) |
| Database | PostgreSQL 17 (one per service) |
| Gateway | Spring Cloud Gateway |
| Frontend | React 19, Vite 8, Tailwind CSS 4 |
| Infra | Docker & Docker Compose |

## Microservices

| Service | Port | DB Port | Description |
|---|---|---|---|
| gateway | 8082 | - | API gateway, routes to all services |
| auth-service | 8086 | 5436 | JWT tokens, user/role/permission management |
| employee-service | 8081 | 5433 | Employee records, CQRS pattern |
| department-service | 8083 | 5434 | Department CRUD, CQRS pattern |
| leave-service | 8084 | 5435 | Leave requests & balances, CQRS pattern |

Each service follows the **CQRS** pattern with separate Command (`CService`) and Query (`QService`) implementations.

## Getting Started

### Prerequisites
- Java 21+
- Node.js 20+
- Docker & Docker Compose
- Maven 3.9+

### Start everything with Docker

```bash
cd backend
docker compose up --build
```

This starts all services and their databases.

### Start databases only

```bash
cd backend
docker compose up -d employee-pg department-pg leave-pg auth-pg
```

### Run services locally

```bash
cd backend/auth-service && mvn spring-boot:run
cd backend/employee-service && mvn spring-boot:run
cd backend/department-service && mvn spring-boot:run
cd backend/leave-service && mvn spring-boot:run
cd backend/gateway && mvn spring-boot:run
```

### Run the frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at http://localhost:5173, Vite proxies `/api` requests to the gateway at port 8082.

## Auth API (`/api/auth`)

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | - | Register a new user |
| POST | `/api/auth/login` | - | Login, returns JWT pair |
| POST | `/api/auth/refresh` | - | Refresh access token |
| GET | `/api/auth/me` | Bearer | Current user info |
| GET | `/api/auth/users` | ADMIN | List all users |
| GET | `/api/auth/users/{id}` | Bearer | Get user by ID |
| PUT | `/api/auth/users/{id}` | Bearer | Update user |
| DELETE | `/api/auth/users/{id}` | ADMIN | Delete user |
| POST | `/api/auth/roles` | ADMIN | Create role |
| GET | `/api/auth/roles` | Bearer | List all roles |
| POST | `/api/auth/users/{userId}/roles/{roleId}` | ADMIN | Assign role |
| POST | `/api/auth/roles/{roleId}/permissions/{name}` | ADMIN | Add permission to role |

Default roles seeded on startup: `USER`, `ADMIN`.

## Employee API (`/api/employees`)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/employees` | Create employee |
| GET | `/api/employees/{id}` | Get by ID |
| GET | `/api/employees/code/{code}` | Get by employee code |
| GET | `/api/employees` | List all (paginated) |
| GET | `/api/employees/department/{id}` | By department |
| GET | `/api/employees/status/{status}` | By employment status |
| GET | `/api/employees/manager/{id}` | By manager |
| GET | `/api/employees/search?keyword=` | Search (paginated) |
| PUT | `/api/employees/{id}` | Update |
| DELETE | `/api/employees/{id}` | Delete |

## Development

```bash
# Backend tests
cd backend && mvn test

# Frontend lint
cd frontend && npm run lint

# Frontend build
cd frontend && npm run build
```

## Status

Active development. Employee, department, leave, and auth services are functional. Frontend UI is being developed.
