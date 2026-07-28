---
kind: skill
id: first-principles
name: first-principles
displayName: FIRST Principles for Test Quality
description: Checklist to validate every generated unit or integration test against Fast, Isolated, Repeatable, Self-validating, Thorough criteria.
---
# FIRST Principles

Apply this checklist to EVERY test before considering it complete, unit or
integration alike.

## Fast
- No `Thread.sleep`; use polling/await utilities or Karate's built-in retry
  only where a real async boundary exists (e.g. eventual consistency after
  a write), and cap retry time explicitly.
- Avoid full Spring context reloads per test class; reuse context where
  Spring's caching allows it.
- A unit test should typically run in well under 100ms; flag anything
  consistently slower for review.

## Isolated / Independent
- No shared mutable static state between tests.
- Each test creates its own test data (or uses a fixture that is reset per
  test) — never depends on data left behind by another test.
- No dependency on execution order; tests must pass when run individually
  or in any shuffled order.

## Repeatable
- Same result on every run, in any environment (local, CI, any timezone).
- Fix or inject time/locale/random seed explicitly rather than relying on
  ambient system state.
- No dependency on external network for unit tests; integration tests
  depend only on the documented, versioned test environment.

## Self-validating
- Pass/fail is determined by an assertion (AssertJ/JUnit matcher, Karate
  `match`), never by a human reading console output or logs.
- Failure messages must be descriptive enough to diagnose without
  re-running in a debugger.

## Thorough
- Cover the happy path AND: boundary values (min, max, empty, null,
  malformed), at least one error/exception path, and at least one
  security-relevant input where the class/endpoint accepts external input.
- Line/branch coverage is a signal, not the goal — a test suite that hits
  100% lines but skips edge cases fails this principle.

## Verification Gate
Before marking any test "done", answer all five checks explicitly (yes/no +
one-line justification). Any "no" blocks completion until resolved.
