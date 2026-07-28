---
kind: task
id: stabilize-test-suite
name: Stabilize Test Suite
description: Iterative root-cause triage loop (max 10 iterations) to bring the combined unit + integration suite to a reliable green state without weakening tests.
---
# Stabilize Test Suite (max 10 iterations)

## Preconditions — verify before iteration 1
1. Business code build passes (unchanged since preflight lint pass).
2. Unit test project builds and runs.
3. App on port **8081** with `DIGITALBANK_PUBLIC_URL` set to its tunnel
   (`agents.md` / `toolchain.publishURLCommand`).
4. Integration (`<ProjectFolderName>QA`) builds and is reachable via
   `DIGITALBANK_PUBLIC_URL`.
If any precondition fails, STOP and report — do not attempt to "fix" tests
against a broken build or unreachable environment.

## Constraints
Follow all Root Guardrails defined in `agents.md`. Task-specific rules:
- Never add `Thread.sleep` to mask timing — use proper polling/await.
- Never delete a test to force green — quarantine it instead (see Flaky tests).

## Loop process — repeat until exit condition
### Each iteration
1. Run the full combined suite (unit + integration); capture pass/fail/
   error counts and stack traces.
2. Triage each failure into exactly one class:
   - Test defect (bad setup, wrong assertion, flaky wait, test data issue)
     → fix it, smallest correct change.
   - Framework/config defect (Karate config, Allure wiring, Spring test
     context wiring) → fix it.
   - Application defect — ONLY if you can point to a specific, documented
     API contract (OpenAPI spec, README, or explicit business requirement)
     that the code violates. If you cannot cite it, classify as
     "undetermined" and keep investigating — never guess.
3. Application defects: do NOT alter or remove the test. Tag it (e.g.
   `@KnownAppDefect` in Karate, `@Disabled("APP-DEFECT-<id>")` in JUnit with
   a linked issue), leave it failing/skipped-with-reason, and log it.
4. Fix with the smallest correct change; one root cause per fix.
5. Re-run affected tests, then the full suite to catch regressions.
6. Record the iteration in `stabilization-log.md`: iteration #, failures
   found, root-cause class, change applied, result.

## Flaky tests
A test that alternates pass/fail across runs is a test defect until proven
otherwise. Diagnose using logs/traces (race condition, shared state,
timing). If unresolved after two attempts, tag `@Quarantine` with a note in
the stabilization log — do not let it block the loop.

## Exit condition (any of)
- All tests pass, excluding those tagged `@KnownAppDefect` or legitimately
  quarantined, OR
- Max 10 iterations reached, OR
- No progress across 2 consecutive iterations (same failures, no new root
  causes) → stop and report the blockers honestly rather than forcing changes.

## Output
1. `stabilization-log.md` — per-iteration audit trail.
2. `defect-log.md` — any `@KnownAppDefect` entries with evidence and the
   specific contract/doc citation.
3. Final merged Allure report path.
4. Summary: total/passed/failed(known-app-defect)/quarantined/skipped, pass
   rate, iterations used, exit-condition caveat if any.
