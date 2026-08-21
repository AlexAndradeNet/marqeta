# DIGITAL BANK - MARQETA

This project simulates the payment-gateway for Marqeta.

---

## ✏️ HOW TO CONTRIBUTE

See [CONTRIBUTE.md](../../CONTRIBUTE.md)

---

## 🧰 TECH STACK

![](https://img.shields.io/badge/JDK-v17-red)
![](https://img.shields.io/badge/Maven-Latest-blue)
![](https://img.shields.io/badge/SpringBoot-Latest-orange)
![](https://img.shields.io/badge/NgRok-Latest-green)

### IDE Plugins

* Lombok
* SonarLint
* Maven
* JUnit
* Sonarlint (optional)

---

## 📗 DOCUMENTATION

* [Confluence page](https://blankfactor.atlassian.net/wiki/spaces/Marqueta/pages/603619801/Project+Overview)
* [Jira Board](https://blankfactor.atlassian.net/jira/software/projects/CMQT/boards/77)
* [Development repository](https://us-east-1.console.aws.amazon.com/codesuite/codecommit/repositories/DigitalBank/browse/refs/heads/develop?region=us-east-1)
* [Integration tests repository](https://us-east-1.console.aws.amazon.com/codesuite/codecommit/repositories/DigitalBankQA/browse?region=us-east-1)

### OpenAPI/Swagger

To see the OpenAPI documentation, you need to run the application and go to the following URL:

http://localhost:8081/swagger-ui/index.html

To see the JSON documentation, you need to run the application and go to the following URL:

http://localhost:8081/v3/api-docs

You can find a fixed version of the JSON documentation in the following
site [Confluence page](https://blankfactor.atlassian.net/wiki/spaces/Marqueta/pages/603619801/Project+Overview).


---

## 🚀 EXECUTION

### Run application in Docker

Database and SonarQube are in a Docker script. To run docker for the first time:

```bash
docker compose up -d
```

### IntelliJ

To run the application in IntelliJ, you need to open the file `src/main/java/com/marqeta/MarqetaApplication.java` and
run the "
main" function.

NOTE: its necessary to have the _database_ and _Core_ project running in IntelliJ/Docker and the _Marqeta_
container stopped.

### Ngrok

The Marqeta's System requires you have a public IP. If you don't have a public IP, you can use ngrok to expose your
localhost to the internet. To do that, you need to run the following command:

```bash
ngrok http 8081
```

This command will give you a public URL like this: `https://7a71-186-113-131-19.ngrok-free.app`

NOTE: If you don't have ngrok installed, you can install it with the following command:

```bash
brew install ngrok
```

### Marqeta integration

You need to create the data to integrate with Marqeta. To do that, you need to call the following endpoints:

| Name                                                    | URL                                                                                                       |
|---------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| 1. Create program funding source                        | `https://sandbox-api.marqeta.com/v3/fundingsources/programgateway`                                        |
| 2. Create card product                                  | `https://sandbox-api.marqeta.com/v3/cardproducts`                                                         |
| 3. Create user                                          | `https://sandbox-api.marqeta.com/v3/users`                                                                |
| 4. Create card                                          | `https://sandbox-api.marqeta.com/v3/cards`                                                                |
| 5. Create account in the Bank (this project's database) | Copy the `token` in the previous step and use it in the field `card_id` in table `accelerator_db.account` |
| 6. Create transaction or Simulate Transaction           | `https://sandbox-api.marqeta.com/v3/simulate/authorization`                                               |
