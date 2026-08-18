<div align="center">

# 👥 Human Resources Platform v2

**A Spring Boot HR backend for users, employers, job seekers, positions, reporting, and relational workforce data.**

![Java](https://img.shields.io/badge/Java-17-E76F00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-REST_API-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Persistence-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Actuator](https://img.shields.io/badge/Spring-Actuator-0EA5E9?style=for-the-badge&logo=springboot&logoColor=white)

</div>

---

## ✨ Overview

Human Resources Platform v2 is a layered Java backend for modeling recruitment and workforce information. It manages users, system users, employers, job seekers, jobs, positions, workplaces, work history, education, languages, and reporting views.

The repository includes REST controllers, service abstractions, DTOs, mappers, JPA repositories, validation, global exception handling, MySQL scripts, and HTML API documentation.

## 🚀 Features

- User and system-user management.
- Employer and job-seeker profiles.
- Job, job-position, workplace, and work-time modeling.
- Education, language, image, department, and city entities.
- Joined DTO views for richer user and employer information.
- Reporting endpoints and pagination support.
- Jakarta validation and centralized exception handling.
- HikariCP connection pooling and Spring Boot Actuator.
- MySQL bootstrap script and API documentation.

## 🧱 Architecture

```text
REST Controllers
      │
      ▼
Service Interfaces & Implementations
      │
      ├──► DTOs & Mappers
      ▼
Spring Data Repositories
      │
      ▼
MySQL
```

```text
├── src/main/java/HRComponents/
│   ├── Controllers/
│   ├── Services/
│   ├── Repostorys/
│   ├── Entitys/
│   ├── DTOs/
│   ├── Mappers/
│   ├── Exceptions/
│   └── GlobalProjectPattern/
├── DataBaseMySQL/DataBase.sql
├── Documentation/UsersDocumentation/APIUsersDocumentation.html
└── pom.xml
```

## 🔌 API Snapshot

| Method | Path | Purpose |
|---|---|---|
| `GET` | `/API/users/GetAllUsers` | List users |
| `POST` | `/API/users/AddUsers` | Add a user |
| `GET` | `/API/users/GetUsersByPrivate/{PrivateName}` | Find users by privilege |
| `GET` | `/API/users/GetInfoUsersRroles/{role}` | Retrieve role-based user information |
| `GET` | `/API/Employees/All/Employees` | List employers |
| `GET` | `/api/JobSeeker/getAllJobSeekerAndEmployer` | Retrieve joined job-seeker and employer data |
| `GET` | `/api/Report/info/AllUsersReport/{Privilege}` | Generate a user report by privilege |

Additional API notes are available in [`Documentation/UsersDocumentation/APIUsersDocumentation.html`](Documentation/UsersDocumentation/APIUsersDocumentation.html).

## ⚙️ Getting Started

### Prerequisites

- JDK 17
- Maven
- MySQL

### Database

1. Create a local MySQL database.
2. Review and apply `DataBaseMySQL/DataBase.sql` in a development environment.
3. Configure the following local properties without committing real secrets:

```properties
spring.datasource.url=<your-jdbc-url>
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
```

### Run

```bash
git clone https://github.com/AhmedRmadanMohamed/Human-Resources-v2.0.0.git
cd Human-Resources-v2.0.0
mvn spring-boot:run
```

### Test

```bash
mvn test
```

## 📝 Repository Note

The root `src` directory is the canonical source location. The committed `bin` directory contains duplicated/generated project material and should not be treated as a second source of truth.

## 🗺️ Roadmap

- Move all credentials to environment-based configuration.
- Remove generated build and IDE artifacts from version control.
- Expand automated tests for services and controllers.
- Publish a complete OpenAPI contract.
- Add authentication and role-based authorization.

---

<div align="center">

A practical HR backend focused on relational modeling and layered Spring development.

</div>
