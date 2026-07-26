# INTEGRATION TETS ROBOT FOR BLANKFACTOR DIGITAL BANK - MARQETA

This is the integration testing robot for the Blankfactor Digital Bank emulator
designed for Marqeta.

This robot carries out JSON Schema validations of Marqeta responses. This helps
detect any changes and assess their impact on the Blankfactor Digital Bank.

---

## ✏️ HOW TO CONTRIBUTE

See [CONTRIBUTE.md](CONTRIBUTE.md)

---

## 🧰 TECH STACK

### Requirements

* Java 17
* Maven
* Serenity BDD
* Cucumber
* RestAssured
* JUnit4
* NodeJS (optional)
* SonarQube (optional)

### IDE Plugins

* JUnit
* Cucumber-java
* Gherkin
* Maven
* Lombok
* Sonarlint (optional)

---

## 📗 DOCUMENTATION

* [Confluence page](https://blankfactor.atlassian.net/wiki/spaces/Marqueta/pages/603619801/Project+Overview)
* [Jira Board](https://blankfactor.atlassian.net/jira/software/projects/CMQT/boards/77)
* [Development repository](https://us-east-1.console.aws.amazon.com/codesuite/codecommit/repositories/DigitalBank/browse/refs/heads/develop?region=us-east-1)
* [Integration tests repository](https://us-east-1.console.aws.amazon.com/codesuite/codecommit/repositories/DigitalBankQA/browse?region=us-east-1)

---

## 🚀 EXECUTION

Prior execution you must create **System variables** or a `.env` file in the
root of the project or create some environment variables with the following
values:

```
MARQETA_API_USERNAME=myusername
MARQETA_API_PASSWORD=mypassword
BF_MARQETA_PUBLIC_URL=https://url.public.com
BF_CORE_PUBLIC_URL=https://url.public.or.local.com
```

Prior to launch executions you need to have the Blankfactor Bank service (the
main project) running locally (with ngrok) or in AWS. The CORE url would be a
public or local url.

### Local launch

To local launch you need to go to the root of the project and execute:

```bash
sh scripts/docker-run-all-apps.sh
```

And you have to expose Marqeta to the internet with ngrok:

```bash
ngrok http 8081
```

The resultant url will be used in the `.env` file as `BF_MARQETA_PUBLIC_URL`
variable.

**NOTE**: exposing the Core (bank) it's not necessary.

### Run all tests

```bash
mvn clean verify
```
``
### Run a specific test

You have two options. You can do it through the command line:

```bash
mvn clean verify -Dcucumber.options="--tags @tag"
```

You can do it through the IDE opening the feature file and clicking on the play
button.

### Open the test report

After the execution with the command line, you can open the report in the
following
path [target/site/serenity/index.html](target/site/serenity/index.html).

---

## ✏️ SERENITY + SCREENPLAY PATTERN

**Want to learn more?** For more information about Serenity BDD, you can read
the [Serenity BDD Book](https://serenity-bdd.github.io/theserenitybook/latest/index.html),
the official online Serenity documentation source. Other sources include:

* [Byte-sized Serenity BDD](https://www.youtube.com/channel/UCav6-dPEUiLbnu-rgpy7_bw/featured)
  tips and tricks about Serenity BDD
* [Serenity BDD Blog](https://johnfergusonsmart.com/category/serenity-bdd/)
  regular articles
  about Serenity BDD
* [The Serenity Dojo](https://www.serenity-dojo.com) - Tailored BDD and Test
  Automation Training and Mentoring
