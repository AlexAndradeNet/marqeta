#!/bin/sh

function fnPrintResultMessage() {
  echo "."
  echo "."
  echo "################> $1"
  echo "."
  echo "."
}

function fnCheckIfSonarIsRunning() {
    host="localhost"
    port="9000"

    if command -v nc &> /dev/null; then
        if nc -z "$host" "$port"; then
            echo "SonarQube is up."
        else
            fnPrintResultMessage "ERROR: SonarQube is down. Check file /CONTRIBUTE.md to know how to start it."
            exit 1
        fi
    else
        fnPrintResultMessage "WARNING: I cannot check if SonarQube is running due the NC command isn't installed."
        fnPrintResultMessage "WARNING: The process will continue, but you have to check manually its result."
    fi
}

fnMain() {
  # Get the directory of the script
  SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

  # Change the current working directory to the script directory
  cd "$SCRIPT_DIR" || exit

  cd ../core;mvn clean verify sonar:sonar && cd ../marqeta;mvn clean verify sonar:sonar

  if [ $? -eq 0 ]; then
      fnPrintResultMessage "SUCCESS: SonarQube scan passed. You can see results in http://localhost:9000"
  else
      fnPrintResultMessage "ERROR: SonarQube scan failed. Fix errors in your code first."
  fi

  exit $?
}

fnCheckIfSonarIsRunning
fnMain

exit 0
