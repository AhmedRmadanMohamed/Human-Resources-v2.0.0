# Contributing

## Development workflow

1. Create a branch from master.
2. Keep changes focused and include tests.
3. Run ./mvnw verify.
4. Update API, configuration, and migration documentation.
5. Open a pull request and wait for CI, dependency review, and CodeQL.

## Coding conventions

- Use Java 17 language features.
- Prefer constructor injection.
- Keep controllers thin and transaction boundaries in services.
- Use request and response DTOs; never serialize JPA entities directly.
- Use database-backed pagination for collection endpoints.
- Do not expose internal exception messages.
- Add Flyway migrations instead of editing an existing applied migration.

## Commit style

Use concise imperative commits, for example:

- feat: add employer profile endpoint
- fix: prevent duplicate user email
- security: restrict report access
- test: cover invalid login
