# Dependabot + Claude Code Demo

A Spring Boot REST API that demonstrates Dependabot and Claude Code working together on GitHub. When Dependabot opens a PR to update a dependency, Claude Code automatically reviews the changes, runs tests, and posts a recommendation.

## Prerequisites

- Java 17+
- An Anthropic API key (for Claude Code Action)

## Running Locally

```bash
# Build and run tests
./mvnw clean verify

# Start the application
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`. An H2 console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:demodb`, user: `sa`, no password).

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/items` | List all items |
| GET | `/api/items?search=query` | Search items by name |
| GET | `/api/items/{id}` | Get item by ID |
| POST | `/api/items` | Create an item |
| PUT | `/api/items/{id}` | Update an item |
| DELETE | `/api/items/{id}` | Delete an item |

### Example requests

```bash
# Create an item
curl -X POST http://localhost:8080/api/items \
  -H "Content-Type: application/json" \
  -d '{"name": "widget", "description": "A test widget", "quantity": 5}'

# List all items
curl http://localhost:8080/api/items

# Search
curl "http://localhost:8080/api/items?search=widget"
```

## Running Tests

```bash
./mvnw test
```

## GitHub Setup

### 1. Add your Anthropic API key

Go to your repo **Settings > Secrets and variables > Actions** and add:

- `ANTHROPIC_API_KEY` — your key from [console.anthropic.com](https://console.anthropic.com)

### 2. What happens automatically

- **Dependabot** checks for outdated Maven dependencies daily and GitHub Actions weekly, then opens PRs.
- **CI** (`ci.yml`) runs build + tests on every PR.
- **Claude Code Review** (`claude-review.yml`) triggers on Dependabot PRs and reviews the dependency update — checking for breaking changes, running tests, and posting a recommendation.
- **Claude Interactive** (`claude-interactive.yml`) lets you mention `@claude` in any PR comment for on-demand help.

### Intentionally outdated dependencies

These are pinned to old versions so Dependabot has something to flag:

| Dependency | Pinned Version | Why |
|---|---|---|
| Spring Boot | 3.1.0 | Several minor versions behind |
| Guava | 31.0.1-jre | 2 major versions behind (current: 33.x) |
| Commons Text | 1.9 | Has CVE-2022-42889 — Dependabot will flag as a security update |
