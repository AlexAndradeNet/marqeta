---
kind: skill
id: karate-allure-setup
name: karate-allure-setup
displayName: Karate DSL + Allure Setup Checklist
description: Portable checklist for scaffolding a Karate DSL test project against a Spring Boot API and wiring Allure reporting.
---
# Karate + Allure Setup Checklist

## Project Scaffolding
- Package manager must match the business code's.
- Java version must match (or be compatible with) the business code's
  target JDK.
- Dependencies (Maven coordinates, pin to versions compatible with the
  business code's Spring Boot/Java version):
  - `com.intuit.karate:karate-junit5`
  - `io.qameta.allure:allure-karate` (or the current maintained Karate-Allure
    adapter — verify latest compatible artifact before pinning)
  - `org.junit.jupiter:junit-jupiter` (runner glue)

## Directory Layout
```
<ProjectFolderName>QA/
├── pom.xml
├── src/test/java/karate/
│   ├── <domain1>/
│   │   ├── <Domain1>Runner.java
│   │   └── <domain1>.feature
│   └── <domain2>/...
├── src/test/resources/
│   ├── karate-config.js
│   └── allure.properties
└── README.md
```

## karate-config.js essentials
- Externalize `baseUrl` per environment (local/ci/staging) via a `karate.env`
  switch — never hardcode.
- Set sane default timeouts (connect/read) explicitly.

## Allure Wiring
- Confirm the Allure adapter writes to `<buildDir>/allure-results`; align 
  this with the orchestrator's merge step so unit and integration results can 
  be combined into one report.
- Verify with a small smoke feature first: run it, confirm
  `*-result.json` files appear, before writing the full suite.
- Common failure mode: adapter dependency present but no result files
  written — check for a required system property
  (`-Dkarate.options`/adapter activation flag) missing from the test
  runner configuration.

## Runner Pattern
Each domain gets one JUnit5 runner class using
`Karate.run("classpath:karate/<domain>").relativeTo(getClass())`, tagged so
CI can select subsets (e.g. `@security`, `@smoke`, `@regression`).

## Verification Gate
Before declaring the QA project ready: running the tests must
pass on the scaffolded smoke feature, and Allure results must be visibly
generated in the expected directory — confirm both before writing the full
scenario set.
