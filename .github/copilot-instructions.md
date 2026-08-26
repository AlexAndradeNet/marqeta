# Copilot instructions for this repository

## Repository shape
- `DigitalBank/core`: Spring Boot 3 / Java 17 bank emulator. Uses a hexagonal layout with `controller`, `service`, `port`, `adapter`, `mapper`, `model`, `repository`, and `dto` packages.
- `DigitalBank/marqeta`: Spring Boot 3 / Java 17 gateway that talks to Marqeta and the Core app through Feign clients.
- `DigitalBankQA`: Java 21 Serenity + Cucumber + Screenplay integration suite for Marqeta/Core flows.

## Build, test, and lint
- Root: `sh scripts/docker-run-all-apps.sh`
- Core (`DigitalBank/core`):
  - Full build/test: `mvn clean verify`
  - Single test: `mvn -Dtest=AccountControllerTest test`
  - Lint/format: `mvn spotless:apply`
  - Sonar: `mvn clean verify sonar:sonar`
  - Mutation coverage: `mvn clean test pitest:mutationCoverage`
- Marqeta (`DigitalBank/marqeta`):
  - Full build/test: `mvn clean verify`
  - Single test: `mvn -Dtest=SomeTestClass test`
  - Lint/format: `mvn spotless:apply`
  - Sonar: `mvn clean verify sonar:sonar`
- QA (`DigitalBankQA`):
  - Full suite: `mvn clean verify`
  - Single tagged run: `mvn clean verify -Dcucumber.options="--tags @tag"`
  - Gherkin lint: `sh scripts/gherkin-lint.sh`

## High-level architecture
- Core is the bank/emulator service. Controllers expose `/port/...` endpoints, services hold business rules, ports define boundaries, adapters implement persistence and external calls, and Feign is used for the ledger integration.
- Core’s error handling is centralized in `AuthorizationCommonController`; validation failures return a `400` response with a `message` field and, when relevant, a token.
- Marqeta is the payment-gateway layer. It creates users/cards/products/funding sources in the Marqeta sandbox and calls Core through `CoreAuthGatewayClient`.
- QA is scenario-driven. Feature files live under `src/test/resources/features`, step definitions live under `src/test/java/com/blankfactor/marqeta/digitalbank/qa/steps`, and JSON schemas live under `src/test/resources/schemas`.

## Key conventions
- Keep the existing package split: `dto/request`, `dto/response`, `mapper/impl`, `port/impl`, `service/impl`, `adapter`, `repository`.
- Use constructor injection with Lombok `@RequiredArgsConstructor` and final fields where the codebase already does.
- Preserve the existing `/* BlankFactor (C)YYYY */` header style; Spotless enforces Java, SQL, and JSON formatting.
- Core tests are Spring Boot integration-style tests with `MockMvc`, `MockWebServer`, and transactional rollback through `BaseTest`.
- QA scenarios are tag-driven; the Gherkin linter rejects `@wip` and `@watch`, duplicate tags, unnamed scenarios, and other malformed feature files.
- When working on QA, keep the required runtime variables in mind: `MARQETA_API_USERNAME`, `MARQETA_API_PASSWORD`, `BF_MARQETA_PUBLIC_URL`, and `BF_CORE_PUBLIC_URL`.
