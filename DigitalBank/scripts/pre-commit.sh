#!/bin/sh

function fnPrintResultMessage() {
  echo "."
  echo "."
  echo "################> $1"
  echo "."
  echo "."
}

function fnMain() {
  # Get the directory of the script
  SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

  # Change the current working directory to the script directory
  cd "$SCRIPT_DIR" || exit

  cd ../core;mvn spotless:apply && cd ../marqeta;mvn spotless:apply

  if [ $? -eq 0 ]; then
      fnPrintResultMessage "SUCCESS: Linter (Spotless) scan passed"
  else
      fnPrintResultMessage "ERROR:  Linter (Spotless) scan failed"
  fi

  exit $?
}

fnMain

exit 0
