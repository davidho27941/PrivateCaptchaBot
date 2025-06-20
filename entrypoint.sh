#!/bin/sh
set -e

if [ -z "$API_KEY" ]; then
  echo "Error: API_KEY environment variable is not set" >&2
  exit 1
fi

exec java -jar /app/app.jar
