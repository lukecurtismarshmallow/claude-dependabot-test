# Project: Dependabot Demo

A Spring Boot 3.1 REST API demo app using Java 17, H2 in-memory database, Guava, and Apache Commons Text.

## Build & Test

```bash
mvn clean verify --batch-mode
```

## Architecture

- **Controller**: `ItemController` - REST endpoints at `/api/items`
- **Service**: `ItemService` - Business logic, uses Guava (Preconditions, Strings) and Commons Text (WordUtils)
- **Repository**: `ItemRepository` - Spring Data JPA interface
- **Model**: `Item` - JPA entity with name, description, quantity

## Dependency Review Guidelines

When reviewing dependency update PRs:

1. Run `mvn clean verify --batch-mode` to ensure tests pass
2. Check if the updated library has breaking API changes that affect `ItemService.java`
3. Key usages to verify:
   - `com.google.common.base.Preconditions` (Guava)
   - `com.google.common.base.Strings` (Guava)
   - `org.apache.commons.text.WordUtils` (Commons Text)
4. For Spring Boot updates, verify H2 and JPA auto-configuration still works
