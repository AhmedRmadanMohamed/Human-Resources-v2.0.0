# Changelog

## 3.0.0 - 2026-08-19

### Security

- Added stateless JWT authentication and role-based authorization.
- Added BCrypt password hashing and removed passwords from response models.
- Replaced committed credentials with required environment variables.
- Restricted CORS and sanitized error responses.

### Data

- Replaced the duplicate legacy schema with normalized Flyway migrations.
- Added unique constraints, indexes, checks, and consistent foreign keys.
- Added database-backed pagination.

### Quality

- Upgraded to Spring Boot 3.5.16 and Java 17.
- Made Lombok annotation processing explicit for reproducible builds on modern JDKs.
- Added H2-backed integration tests, JaCoCo, Maven Wrapper, OpenAPI, Docker, CI, CodeQL, dependency review, and Dependabot.
- Replaced mixed-case legacy endpoints with /api/v1.
