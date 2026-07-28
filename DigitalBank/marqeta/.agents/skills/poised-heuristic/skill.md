---
kind: skill
id: poised-heuristic
name: poised-heuristic
displayName: POISED Heuristic for API Integration Test Design
description: Structured checklist (Parameters, Output, Interoperability, Security, Errors, Data/Documentation, Reporting) for designing Karate DSL integration test coverage per endpoint.
---
# POISED Heuristic

For every REST endpoint under test, walk through all seven dimensions
explicitly. If a dimension does not apply, record why — never skip silently.

## P — Parameters
- Validate every input's data type and range.
- Boundary tests: min, max, empty string/array, null, malformed
  (wrong type, wrong format, oversized payload).
- Cover required vs optional fields; omission of a required field must
  return a clear 4xx.

## O — Output
- Verify response status code for the happy path.
- Verify response schema/structure (field presence, types, nesting) against
  the API contract (OpenAPI spec if available).
- Verify response content correctness, not just shape (actual values match
  expected business outcome).

## I — Interoperability
- Test integration behavior with dependent systems/services (downstream
  APIs, message queues, database side effects triggered by the call).
- Verify correct propagation of correlation/trace IDs if the architecture
  uses them.

## S — Security
- Validate auth tokens: missing, expired, malformed, wrong scope.
- Validate permission levels: role that should be denied gets 401/403;
  role that should be allowed gets through.
- Validate sensitive data is not leaked in responses/logs (PII masking,
  no stack traces in production-mode error responses).

## E — Errors
- Verify HTTP response codes for every failure-handling path (400, 401,
  403, 404, 409, 422, 500 as applicable to the endpoint's contract).
- Verify error response body structure is consistent across endpoints.

## D — Data / Documentation
- Confirm state persistence after write operations (a POST/PUT/DELETE's
  effect is verifiable via a follow-up GET or direct data-layer check).
- Validate that the endpoint behaves as documented (OpenAPI/README);
  flag any drift between docs and actual behavior as a finding, do not
  silently "fix" the docs or the test to match.

## R — Reporting
- Every scenario above must emit a result into Allure (feature, scenario,
  tags for the POISED dimension it covers, e.g. `@security`, `@errors`).
- Failed scenarios must attach the request/response payload as evidence.

## Coverage Ledger
Maintain one row per endpoint per dimension: dimension -> scenario(s) ->
Karate feature/scenario name -> status. This ledger feeds
`integration-coverage-map.json`.
