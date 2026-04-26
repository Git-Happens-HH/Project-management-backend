#!/usr/bin/env bash
set -euo pipefail

if [ "$#" -lt 3 ]; then
  echo "Usage: $0 <namespace> <image> <app-name>"
  exit 1
fi

NAMESPACE="$1"
IMAGE="$2"
APP_NAME="$3"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "[deploy] Switching project to ${NAMESPACE}"
oc project "${NAMESPACE}"

echo "[deploy] Applying manifests"
oc apply -f "${SCRIPT_DIR}/service.yaml"
oc apply -f "${SCRIPT_DIR}/route.yaml"
oc apply -f "${SCRIPT_DIR}/deployment.yaml"

echo "[deploy] Setting image ${IMAGE}"
oc set image "deployment/${APP_NAME}" "${APP_NAME}=${IMAGE}" --record=true

echo "[deploy] Image updated; rollout is triggered by deployment change"
