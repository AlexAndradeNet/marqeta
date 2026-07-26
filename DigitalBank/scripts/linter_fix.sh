#!/bin/sh

  # Get the directory of the script
  SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

  # Change the current working directory to the script directory
  cd "$SCRIPT_DIR" || exit

sh pre-commit.sh
