# Serenity Marqeta / Blankfactor Bruno Collection

Bruno collection generated from Serenity BDD test results (report dated 25-07-2026).

## Overview

The Serenity suite exercised the following Marqeta Sandbox + local Blankfactor flows:

| Scenario | Description |
|----------|-------------|
| Authorize a transaction | Full setup + simulate authorization |
| Reverse a previous transaction | Setup + auth + simulate reversal |
| Clear a previous transaction | Setup + auth + simulate clearing |
| Mimic reversal | Setup + auth + post mimic reversal webhook |
| Mimic clearing | Setup + auth + post mimic clearing webhook |

All 5 test cases **passed**.

## Collection Structure

```
bruno-serenity-marqeta/
├── bruno.json
├── environments/
│   └── sandbox.bru          # Base URLs, tokens, credentials
├── Setup/
│   ├── 01-Create Funding Source.bru
│   ├── 02-Create Card Product.bru
│   ├── 03-Create User.bru
│   └── 04-Create Card.bru
├── Local/
│   └── 05-Create Local Account.bru
├── Simulate/
│   ├── 06-Simulate Authorization.bru
│   ├── 07-Simulate Reversal.bru
│   └── 08-Simulate Clearing.bru
└── Webhooks/
    └── 09-Post Webhook (Mimic).bru
```

## How to use

1. Open the folder in [Bruno](https://www.usebruno.com/).
2. Select the **sandbox** environment.
3. Update ephemeral values:
   - `fundingSourceUrl` / `webhookEndpoint` (ngrok or your tunnel)
   - `cardProductToken` (must be unique)
   - After each create step, copy returned tokens into env vars:
     - `fundingSourceToken`
     - `cardToken`
     - `originalTxnToken`
4. Run requests in sequence (Setup → Local → Simulate / Webhooks).

## Credentials (from Serenity report)

- Application token: `9b752ef3-60fb-43ce-a500-0696ebd8dc83`
- Access token: `186d4acc-896d-4b30-9013-2c3821296974`
- Auth header is pre-encoded as Basic.

> **Note**: These are sandbox credentials captured in the test report. Rotate if needed.

## Notes

- Local service expected at `http://localhost:8080`.
- Webhook & JIT URLs point to an ngrok tunnel that was active during the original run.
- Card product body includes full fulfillment / JIT funding configuration exactly as used in the Serenity tests.
- The webhook request body is a representative payload from a mimic-reversal scenario; adjust `type` for clearing.
