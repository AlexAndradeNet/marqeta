---
kind: config
id: root-guidelines
name: Java Spring Boot QA Bundle Guidelines
description: Root guardrails and documentation for the unit + integration test generation bundle
Version: 1.4.1
Protocol: Dot Agents
---

# Java/Spring Boot API Testing — Agents Bundle

`.agents`-protocol compliant bundle that plans, reviews, then generates JUnit
unit tests and Karate DSL integration tests for an existing Spring Boot REST
API, with combined Allure reporting.

## Technology Stack
- Language: Java
- Framework: Spring Boot
- API: REST
- Unit Testing: JUnit 5
- Integration Testing: Karate DSL
- Reporting: Allure
- Build tool: Maven
- **App listen port: `8081`** (`http://localhost:8081`). Integration tests reach it via a tunnel of this port; set `DIGITALBANK_PUBLIC_URL` to that public URL (`toolchain.publishURLCommand`).

## External servers
- Ledger: http://localhost:8087
- Core bank backend: http://localhost:8080
- Core bank DB: jdbc:postgresql://localhost:5432/accelerator_db

## Credentials for Integration Tests
The test suite should read the following environment variables before execution
(names only — never hardcode values; never read `.env*` files):
```
DIGITALBANK_PUBLIC_URL
MARQETA_API_USERNAME
MARQETA_API_PASSWORD
```

## Mission
Plan and review test design first, then generate unit tests (JUnit) and
integration tests (Karate DSL) for an existing Spring Boot REST API **without
ever touching business logic**, and report combined coverage via Allure.

## Install

- Copy the `.agents/` directory into the root of your business code repository. Any `.agents`-protocol-compatible tool will pick it up automatically.
    ```
    YourSpringBootApi/
    ├── .agents/          ← this bundle
    ├── src/main/java/...
    └── pom.xml (or build.gradle)
    ```
- Compile for your AI Agent: `npx @sentry/dotagents install`.

## Execution

### Antigravity

Run `agy`
Verify your configuration
`/inspect`
Open the Agent Manager Panel
`/agents`
Interactive Guardrail Verification
`/grill me`
Execute
`/go`

## Bundle Contents

| Path | Purpose |
|------|---------|
| `agents.md` | This file – root guardrails + documentation |
| `agents/qa-bundle-orchestrator/` | Orchestrator: preflight, plan → review → parallel code, reconcile, report |
| `agents/test-plan-agent/` | Designs unit + integration plans (`memories/*-test-plan.md`) |
| `agents/plan-reviewer-agent/` | Reviews and adjusts both plans before coding |
| `agents/unit-test-agent/` | Code-only: implements `memories/unit-test-plan.md` |
| `agents/integration-test-agent/` | Code-only: implements `memories/integration-test-plan.md` in sibling QA |
| `memories/` | Architecture, unit/integration plans, review logs |
| `skills/first-principles/` | FIRST checklist |
| `skills/poised-heuristic/` | POISED design checklist |
| `skills/karate-allure-setup/` | Karate + Allure scaffolding checklist |
| `tasks/stabilize-test-suite/` | Optional iterative stabilization loop |
| `toolchain.yaml` | Build/test/formatter/publish-URL commands |
| `profiles/` | Tool permission profiles |

## How to Run / Orchestration Order (strict)
Invoke the orchestrator agent (`qa-bundle-orchestrator`) with your business code root path:

1. **Preflight (blocking):** external servers (DB, core, ledger) must be up — stop everything if not; then formatter once, architecture note
2. `test-plan-agent` writes `memories/unit-test-plan.md` and `memories/integration-test-plan.md` (new/modify/delete + reason per entry)
3. `plan-reviewer-agent` reviews and adjusts both plans
4. **In parallel:** `unit-test-agent` and `integration-test-agent` implement only their approved plans
5. Orchestrator reconciles overlap (unit wins) and merges Allure results
6. (Optional) `stabilize-test-suite` (max 10 iterations)
7. Formatter over final tests + combined Allure / Markdown coverage report

## Deliverables Checklist
- [ ] External servers verified up before any other work
- [ ] Formatter executed, no business logic changes
- [ ] Unit and integration plans written and reviewed
- [ ] Unit tests implemented from plan only (no infrastructure mocking)
- [ ] `<ProjectName>QA` folder with Karate tests from plan only; stack-synced with business project
- [ ] Allure Reports integrated and generating output
- [ ] Final combined coverage report (unit + integration, shown separately)
- [ ] All tests validated against FIRST principles

## Global Guardrails (apply to every agent – no exceptions)
This section is the single source of truth. Agents reference it; they do not restate it in full.

- NEVER read `.env*` files or hardcode secret values; only use named environment variables listed in this file.
- NEVER modify business code logic. The only permitted change to the business code repository is running the project’s already-configured formatter once, at the very start (formatting only).
- NEVER weaken, remove, or loosen an assertion to force a test to pass.
- NEVER mock infrastructure (DB, queues, external services) in unit tests. Use real infrastructure or lightweight equivalents (H2 / embedded Postgres, embedded Kafka, WireMock only for genuinely external third-party APIs).
- On overlapping coverage between unit and integration tests, the **UNIT TEST WINS**. Remove or narrow the redundant integration scenario, never the unit test.
- Every generated test must satisfy FIRST (see `skills/first-principles`).
- Every integration test must be designed against POISED (see `skills/poised-heuristic`).
- All test results (unit + integration) must surface in Allure Reports.
- Stop and report — do not guess — if project structure, package manager, or dependency versions cannot be determined with confidence.
- Code-writing agents implement **only** the approved plan for their layer; they do not expand scope.

## Repository Layout Convention
- Business code root: `<ProjectFolderName>/`
- QA sibling root: `<ProjectFolderName>QA/` (created next to, not inside, the business code root)
- QA root uses the same package manager, language version, and formatter as the business code; Karate/Allure versions must be compatible with that stack.

## Model / thinking modes
Declared in each agent `config.json` (`modelConfig`) and `agent.md` frontmatter (`thinking`):

| Agent | Mode | Intent |
|-------|------|--------|
| `test-plan-agent` | `reasoning` (effort: high) | Deep design of unit + integration plans |
| `plan-reviewer-agent` | `reasoning` (effort: high) | Critique and adjust plans |
| `unit-test-agent` | `standard` | Implement approved unit plan only |
| `integration-test-agent` | `standard` | Implement approved integration plan only |
| `qa-bundle-orchestrator` | `standard` | Sequence phases and gates |

Hosts that ignore unknown keys still get correct behavior from agent prompts (plan agents never write tests; code agents never invent scenarios). Verify with `/inspect` or Agent Manager that reasoning agents bind to a deeper/thinking model when the runtime supports it.

## Key Design Decisions
- **Plan → review → parallel code** – design is separated from implementation
- **Unit and integration code agents run in parallel** after plans are approved
- **Skills carry reusable logic** – FIRST, POISED and Karate+Allure setup are portable
- **Guardrails live once in this file** – agents inherit by reference, not copy
- **Unit tests win on overlap** – keeps the suite lean and honest
