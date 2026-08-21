# DIGITAL BANK

This project simulates be a bank in a payment-gateway process.

## 📗 DOCUMENTATION

* [Confluence page](https://blankfactor.atlassian.net/wiki/spaces/Marqueta/pages/603619801/Project+Overview)
* [Jira Board](https://blankfactor.atlassian.net/jira/software/projects/CMQT/boards/77)
* [Development repository](https://us-east-1.console.aws.amazon.com/codesuite/codecommit/repositories/DigitalBank/browse/refs/heads/develop?region=us-east-1)
* [Integration tests repository](https://us-east-1.console.aws.amazon.com/codesuite/codecommit/repositories/DigitalBankQA/browse?region=us-east-1)

---

## About this repo

This is a mono-repo project, with the following characteristics:

* No language specific. New projects can be added in any language.
* Architecture: Clean Architecture.

This repo contains the following projects:

* Core: the bank or authorizer + database: Java + PostgreSQL.
* Marqeta: a payment gateway: Java.

---

## ✏️ HOW TO CONTRIBUTE

See [CONTRIBUTE.md](../CONTRIBUTE.md)

---

## ✅ MONOREPO MANAGEMENT

Every project has its own README.md file with instructions to run it locally.
You can run each project separately, but if you want to run all projects at the same time, you can use the following
tools (scripts):

### Tools

* Dockerize all projects: `sh ../scripts/docker-run-all-apps.sh`
* Run sonarqube in all projects: `sh ../scripts/sonar_check.sh`
