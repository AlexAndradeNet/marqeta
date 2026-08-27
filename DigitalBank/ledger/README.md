# DigitalBank Webhook Mock Server

This project contains a minimal Node.js HTTP server that mimics a webhook endpoint for DigitalBank accounting reservation events.

## Endpoint

- Method: `POST`
- URL: `/v1/parties/digitalbank/accounting-transaction/reservation/webhook`
- Port: `8087`

## Response

The server returns HTTP `200 OK` with the following JSON body:

```json
{
  "versionResponse": "alpha",
  "reservationId": "8afa90f9-5fec-428f-bd17-aec9cc657284"
}
```

## Run locally

```bash
node server.mjs
```

The server will start listening on:

```text
http://localhost:8087
```

## Example request

```bash
curl -i -X POST http://localhost:8087/v1/parties/digitalbank/accounting-transaction/reservation/webhook
```

## Run with Docker

Build and start the container:

```bash
docker compose up --build
```

Or build and run directly:

```bash
docker build -t digitalbank-webhook .
docker run -p 8087:8087 digitalbank-webhook
```
