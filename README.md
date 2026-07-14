# HRMS - Human Resource Management System

A full-stack Human Resource Management System built with **Spring Boot 3.4.5** (Java 21) on the backend and **React 19** with **Vite** on the frontend.

## Architecture Overview

```
humanresourcemanagement/
├── backend/           # Spring Boot REST API
│   ├── src/
│   │   ├── main/java/com/naren/humanresourcemanagement/
│   │   │   ├── Controller/        # REST controllers
│   │   │   ├── DTO/               # Data Transfer Objects
│   │   │   │   └── mapper/        # DTO mappers
│   │   │   ├── Entity/            # JPA entities
│   │   │   │   └── Enums/         # Enum types
│   │   │   ├── Exception/         # Custom exceptions
│   │   │   ├── Repository/        # JPA repositories
│   │   │   └── Service/
│   │   │       └── Impl/          # Service implementations (CQRS)
│   │   └── resources/             # Application config
│   ├── Dockerfile
│   └── docker-compose.yaml
├── frontend/          # React + Vite SPA
│   ├── src/
│   │   ├── App.jsx
│   │   ├── App.css
│   │   ├── index.css
│   │   └── main.jsx
│   └── public/
└── .gitignore
```

## Tech Stack

### Backend
| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.5 |
| PostgreSQL | 17 |
| Maven | 3.9+ |
| Lombok | Latest |
| Hibernate (JPA) | Via Spring Boot |

### Frontend
| Technology | Version |
|---|---|
| React | 19.2.7 |
| Vite | 8.1.1 |
| Tailwind CSS | 4.3.2 |
| Oxlint | 1.71.0 |

### Infrastructure
- **Docker** & Docker Compose for containerized deployment
- **PostgreSQL 17** as the primary database

## Features

### Domain Entities
- **Employee** - Core employee records with personal info, contact details, department assignment, position, manager hierarchy, shift assignment
- **Department** - Departments with manager and location
- **Position** - Job titles with grade and base salary
- **Attendance** - Daily check-in/check-out tracking with working hours
- **Leave Request** - Leave applications with type (Sick, Casual, Annual, etc.), approval workflow
- **Payroll** - Monthly salary processing with basic salary, bonus, allowance, deduction, tax, and net salary
- **Performance Review** - Employee performance evaluations
- **Document** - Employee document management
- **Notification** - System notifications for employees
- **Shift** - Work shift definitions
- **User** - System user accounts
- **Holiday** - Company holiday calendar
- **Role** - User role definitions

### CQRS Pattern
The application implements **CQRS (Command Query Responsibility Segregation)** for employees:
- **EmployeeCService** (`@Primary`) - Handles create, update, delete operations
- **EmployeeQService** - Handles read/query operations (get by id, search, filter by department/status/manager)

### REST API Endpoints (`/api/employees`)

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/employees` | Create a new employee |
| `GET` | `/api/employees/{id}` | Get employee by ID |
| `GET` | `/api/employees/code/{employeeCode}` | Get employee by employee code |
| `GET` | `/api/employees` | Get all employees (paginated) |
| `GET` | `/api/employees/department/{departmentId}` | Get employees by department |
| `GET` | `/api/employees/status/{status}` | Get employees by employment status |
| `GET` | `/api/employees/manager/{managerId}` | Get employees by manager |
| `GET` | `/api/employees/search?keyword=` | Search employees by keyword (paginated) |
| `PUT` | `/api/employees/{id}` | Update an employee |
| `DELETE` | `/api/employees/{id}` | Delete an employee |

### Employee Fields
- `id`, `employeeCode`, `firstName`, `lastName`, `email`, `phone`, `gender`, `dateOfBirth`, `hireDate`, `employmentStatus`, `address`, `city`, `state`, `country`, `postalCode`, `emergencyContactName`, `emergencyContactPhone`
- Relationships: `department`, `position`, `manager`, `shift`

### Search & Filtering
- Full-text search across `firstName`, `lastName`, `email`, `employeeCode`
- Filter by department, employment status, manager
- Paginated responses with Spring Data `Pageable`

## Getting Started

### Prerequisites
- Java 21+
- Node.js 20+
- Docker & Docker Compose (for PostgreSQL)
- Maven 3.9+

### 1. Start PostgreSQL

```bash
cd backend
docker compose up -d pg
```

This starts PostgreSQL 17 on port **5332** with:
- Database: `hrms`
- Username: `hrms_user`
- Password: `hrms_pass`

### 2. Run the Backend

```bash
cd backend
./mvnw spring-boot:run
```

The backend starts on **http://localhost:8080**.

### 3. Run the Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend starts on **http://localhost:5173** (Vite default).

### 4. Full Docker Deployment

To run everything (database + backend) in Docker:

```bash
cd backend
docker compose up --build
```

This starts:
- **PostgreSQL 17** on port **5332**
- **Spring Boot Backend** on port **8080**

## Configuration

### Backend (`backend/src/main/resources/application.yaml`)
- **Server Port**: 8080
- **Database URL**: `jdbc:postgresql://localhost:5332/hrms`
- **JPA**: `ddl-auto: create` (auto-creates tables; change to `update` or `validate` for production)
- **Hibernate**: SQL logging enabled with formatted output

### Database (`backend/docker-compose.yaml`)
- PostgreSQL 17 with health check
- Persistent volume for data storage

### Docker Build (`backend/Dockerfile`)
- Multi-stage build:
  1. **Build stage**: Maven 3.9 + Eclipse Temurin 21 - compiles and packages the JAR
  2. **Runtime stage**: Eclipse Temurin 21 JRE (Alpine) - runs the JAR

## API Examples

### Create Employee

```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@company.com",
    "hireDate": "2026-01-15T00:00:00",
    "employmentStatus": "ACTIVE",
    "departmentId": 1,
    "positionId": 1
  }'
```

### Search Employees

```bash
curl "http://localhost:8080/api/employees/search?keyword=john&page=0&size=10"
```

## Development

### Backend Test Suite

```bash
cd backend
./mvnw test
```

### Frontend Linting

```bash
cd frontend
npm run lint
```

### Frontend Build

```bash
cd frontend
npm run build
```

## Project Status

This project is in active development. The employee CRUD and query API is functional with CQRS pattern. Additional entity endpoints and frontend UI components are being developed.