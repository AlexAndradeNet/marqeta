# Marqeta

This is a monorepo for test the Marqeta's APIs Payment Gateway.

The following are the projects that compose this monorepo:
- [Core Bank](DigitalBank/core/).
- [Marqeta Gateway](DigitalBank/marqeta/)
    - [AI Agents](DigitalBank/marqeta/.agents) for develop Unit and Integration Tests.
- [Integration Tests Suite](DigitalBankQA/).

## How to run the project

Minimal requirements:
- Docker
- Ngrok (to expose a public url)
- Marqueta credentials [for developers](https://www.marqeta.com/developer-overview).


## Instructions

### Run the application
1. Move the `.env.sample` files to `.env` and modify the marqeta's project to have the credentials.
2. Start Docker.
3. Run the following script:
    ```bash
    sh scripts/docker-run-all-apps.sh
    ```
4. Expose the URL (more details [here](DigitalBank/marqeta/README.md)):
    ```bash
    ngrok http 8081
    ```

### Run the integration test suite
1. Update the Marqeta's credentials in the `.env` file.
2. Update the public (Ngrok) URL in the `.env` file.

## More info

- Core Bank [README](DigitalBank/core/README.md) file.
- Marqeta Gateway [README](DigitalBank/marqeta/README.md) file.
- Integration Tests Suite [README](DigitalBankQA/README.md) file.

## How to contribute

Follow [these instructions](CONTRIBUTE.md) to contribute to this repo.
