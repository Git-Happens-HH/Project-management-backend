#!/usr/bin/env bash
set -euo pipefail

if [ "$#" -lt 2 ]; then
  echo "Usage: $0 <namespace> <app-name>"
  exit 1
fi

NAMESPACE="$1"
APP_NAME="$2"

oc project "${NAMESPACE}"

echo "[rollback] Rolling back deployment/${APP_NAME}"
oc rollout undo "deployment/${APP_NAME}"

echo "[rollback] Waiting for rollback rollout"
oc rollout status "deployment/${APP_NAME}" --timeout=300s

echo "[rollback] Rollback completed"
