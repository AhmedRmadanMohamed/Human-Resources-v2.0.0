<div align="center">

# 👥 Human Resources API

### Secure workforce and recruitment management with Spring Boot

![Java](https://img.shields.io/badge/Java-17-E76F00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.16-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Security](https://img.shields.io/badge/Security-JWT_%2B_RBAC-0F172A?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.4-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![CI](https://img.shields.io/github/actions/workflow/status/AhmedRmadanMohamed/Human-Resources-v2.0.0/ci.yml?branch=master&style=for-the-badge&label=CI)

[API documentation](#-api-documentation) · [Quick start](#-quick-start) · [Security](SECURITY.md) · [Migration guide](docs/MIGRATION_V3.md)

</div>

---

## ✨ Overview

Human Resources API is a production-oriented Java backend for users, employers, job seekers, jobs, education, languages, work history, and reporting.

Version 3 replaces the legacy implementation with a secure and testable architecture:

- stateless JWT authentication;
- role-based authorization for ADMIN, HR, EMPLOYER, and JOB_SEEKER;
- BCrypt password hashing;
- request validation and safe response DTOs;
- normalized Flyway-managed database schema;
- database-backed pagination capped at 100 records;
- sanitized API errors;
- OpenAPI, Docker, CI, CodeQL, dependency review, and Dependabot.

## 🔐 Security model

- Login, health, and OpenAPI documentation are public.
- User creation requires ADMIN.
- User and report endpoints require ADMIN or HR.
- Employer and job-seeker directories require an authenticated user.
- Passwords are accepted only as request data, stored as BCrypt hashes, and never returned.
- Database credentials and JWT signing keys are required environment variables.
- CORS accepts only configured origins and never uses a wildcard.

## 🧱 Architecture

    HTTP / JSON
        │
        ▼
    REST Controllers
        │
        ▼
    Transactional Services ─────► BCrypt + JWT
        │
        ▼
    Spring Data Repositories
        │
        ▼
    JPA Entities + Flyway
        │
        ▼
    MySQL

JPA entities are never serialized directly. Controllers expose dedicated request and response records.

## 🔌 API

| Method | Endpoint | Access | Purpose |
|---|---|---|---|
| POST | /api/v1/auth/login | Public | Issue a short-lived JWT |
| GET | /api/v1/users | ADMIN, HR | List users |
| POST | /api/v1/users | ADMIN | Create a user |
| GET | /api/v1/users/{id} | ADMIN, HR | Retrieve one user |
| GET | /api/v1/users/by-role/{role} | ADMIN, HR | Filter users by role |
| GET | /api/v1/employers | Authenticated | List employer profiles |
| GET | /api/v1/job-seekers | Authenticated | List or filter job seekers |
| GET | /api/v1/reports/users | ADMIN, HR | Generate a paginated user report |

Collection endpoints support page, size, and sort parameters. The maximum page size is 100.

## 🚀 Quick start

### Prerequisites

- Docker Desktop with Docker Compose, or JDK 17 and MySQL 8
- Git

### Run with Docker

1. Clone the repository:

       git clone https://github.com/AhmedRmadanMohamed/Human-Resources-v2.0.0.git
       cd Human-Resources-v2.0.0

2. Create local configuration:

       cp .env.example .env

3. Replace every placeholder in .env. Generate the JWT signing secret with:

       openssl rand -base64 32

4. Start MySQL and the API:

       docker compose up --build

The API starts on http://localhost:8081.

The optional bootstrap administrator is created only when both BOOTSTRAP_ADMIN_EMAIL and BOOTSTRAP_ADMIN_PASSWORD are present. Remove those values after the account exists.

### Run with Maven

Configure DB_URL, DB_USERNAME, DB_PASSWORD, and JWT_SECRET_BASE64 in your environment, then run:

    ./mvnw spring-boot:run

On Windows:

    mvnw.cmd spring-boot:run

## 🧪 Verification

Tests use an isolated in-memory database and never require development or production credentials:

    ./mvnw verify

The suite validates:

- Flyway migrations and Hibernate schema mappings;
- anonymous access rejection;
- login and JWT issuance;
- administrator-only user creation;
- BCrypt storage and password non-disclosure;
- structured validation errors;
- pagination limits.

JaCoCo writes the HTML coverage report to target/site/jacoco/index.html.

## 🗄️ Database migrations

Flyway is the only schema-management mechanism:

- V1 creates the normalized schema, constraints, and indexes.
- V2 creates the supported system roles.
- Hibernate runs in validate mode and never modifies production tables.

Version 3 expects a new database. Read [the migration guide](docs/MIGRATION_V3.md) before moving legacy data.

## 📖 API documentation

With the application running:

- Swagger UI: http://localhost:8081/swagger-ui.html
- OpenAPI JSON: http://localhost:8081/v3/api-docs
- Health check: http://localhost:8081/actuator/health

Use the Authorize button in Swagger UI with a token returned by the login endpoint.

## ⚙️ Configuration

| Variable | Required | Description |
|---|---:|---|
| DB_URL | Yes | MySQL JDBC URL |
| DB_USERNAME | Yes | Least-privileged database user |
| DB_PASSWORD | Yes | Database password |
| JWT_SECRET_BASE64 | Yes | Base64-encoded random secret of at least 32 bytes |
| JWT_ISSUER | No | JWT issuer; defaults to human-resources-api |
| JWT_TTL | No | ISO-8601 duration; defaults to PT1H |
| CORS_ALLOWED_ORIGINS | No | Comma-separated browser origins |
| BOOTSTRAP_ADMIN_EMAIL | No | Optional first administrator email |
| BOOTSTRAP_ADMIN_PASSWORD | No | Optional first administrator password |
| SERVER_PORT | No | HTTP port; defaults to 8081 |

Never commit a populated .env file.

## 🗂️ Project structure

    src/main/java/com/ahmedramadan/hr/
    ├── api/          REST controllers, DTOs, and response mapping
    ├── config/       Security, JWT, CORS, OpenAPI, and bootstrap
    ├── domain/       JPA domain model
    ├── error/        Sanitized API error handling
    ├── repository/   Spring Data repositories
    ├── security/     UserDetails and HTTP security responses
    └── service/      Authentication and business transactions

    src/main/resources/
    ├── application.properties
    └── db/migration/

## 🛡️ Repository protections

The repository includes:

- CI build and integration tests;
- Gitleaks history scanning;
- CodeQL security-and-quality analysis;
- pull-request dependency review;
- weekly Maven and GitHub Actions updates;
- a private vulnerability reporting policy.

See [CONTRIBUTING.md](CONTRIBUTING.md), [SECURITY.md](SECURITY.md), and [CHANGELOG.md](CHANGELOG.md).

---

<div align="center">

Built as a secure, maintainable Spring backend with explicit data boundaries.

</div>
