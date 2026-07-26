#!/usr/bin/env bash

set -u

fn_print_message() {
  echo
  echo
  echo "################> $1"
  echo
  echo
}

fn_fail() {
  fn_print_message "ERROR: $1"
  exit 1
}

fn_run() {
  "$@"
  local exit_code=$?
  if [ "$exit_code" -ne 0 ]; then
    fn_fail "$* failed with exit code $exit_code"
  fi
}

fn_require_command() {
  command -v "$1" >/dev/null 2>&1 || fn_fail "Required command not found: $1"
}

fn_rebuild_compose_project() {
  local project_dir="$1"
  local project_name="$2"

  fn_print_message "Processing ${project_name}"

  cd "$project_dir" || fn_fail "Cannot change directory to $project_dir"

  if [ ! -f "docker-compose.yml" ] && [ ! -f "docker-compose.yaml" ] && [ ! -f "compose.yml" ] && [ ! -f "compose.yaml" ]; then
    fn_fail "No Compose file found in $project_dir"
  fi

  fn_print_message "Stopping and cleaning previous ${project_name} containers and local images"
  fn_run docker compose down --remove-orphans --rmi local

  fn_print_message "Building ${project_name} images without cache"
  fn_run docker compose build --no-cache

  fn_print_message "Starting ${project_name} containers with forced recreation"
  fn_run docker compose up -d --force-recreate

  fn_print_message "${project_name} is up"
}

fn_main() {
  fn_require_command docker

  local script_dir
  script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)" || fn_fail "Unable to resolve script directory"

  cd "$script_dir" || fn_fail "Cannot change directory to script directory"

  fn_rebuild_compose_project "../core" "Core"
  fn_rebuild_compose_project "../marqeta" "Marqeta"

  fn_print_message "All services rebuilt and started successfully"
  exit 0
}

fn_main
