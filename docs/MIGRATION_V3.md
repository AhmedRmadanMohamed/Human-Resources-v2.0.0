# Migration to v3

Version 3 is a security-focused rewrite and contains intentional breaking changes.

## Before upgrading

1. Back up the existing database.
2. Rotate any database credentials that appeared in Git history.
3. Export required legacy data.
4. Create a new empty database for v3.
5. Configure all environment variables from .env.example.

## Database

Flyway now owns the schema. V1__create_hr_schema.sql creates the normalized schema and V2__seed_system_roles.sql creates the supported roles.

The old SQL dump contained duplicate tables, duplicate foreign keys, and columns that did not match the JPA model. Do not apply it to a v3 database.

For a production system with existing data, write and test an explicit data migration after reviewing the legacy data. Do not point v3 at a legacy schema and rely on Hibernate to modify it.

## API changes

All endpoints now use the /api/v1 prefix:

- POST /api/v1/auth/login
- GET or POST /api/v1/users
- GET /api/v1/users/{id}
- GET /api/v1/users/by-role/{role}
- GET /api/v1/employers
- GET /api/v1/job-seekers
- GET /api/v1/reports/users

Legacy mixed-case endpoints were removed.

## Authentication

Except for login, health, and API documentation, endpoints require a Bearer JWT. User creation requires ADMIN; user and report reads require ADMIN or HR.

Existing plaintext passwords cannot be migrated safely. Require a password reset and store only BCrypt hashes.
