#!/bin/sh

# Usage: . ./export-env.sh
# Requires: an .env file

while IFS='=' read -r key value; do
  if [ -n "$key" ] && [ "${key#\#}" = "$key" ]; then
    export "$key=$value"
  fi
done < ../.env

