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

if [ "${FORCE_DELETE_TERMINATING_PODS:-false}" = "true" ]; then
  echo "[deploy] Cleaning up pods stuck in Terminating state"
  TERMINATING_PODS="$(oc get pods -l "app=${APP_NAME}" -o jsonpath='{range .items[?(@.metadata.deletionTimestamp)]}{.metadata.name}{"\n"}{end}' 2>/dev/null || true)"
  if [ -n "${TERMINATING_PODS}" ]; then
    while IFS= read -r pod; do
      if [ -n "${pod}" ]; then
        echo "[deploy] Force deleting terminating pod ${pod}"
        oc delete pod "${pod}" --grace-period=0 --force || true
      fi
    done <<< "${TERMINATING_PODS}"
  else
    echo "[deploy] No terminating pods found"
  fi
fi

echo "[deploy] Applying manifests"
oc apply -f "${SCRIPT_DIR}/service.yaml"
oc apply -f "${SCRIPT_DIR}/route.yaml"
oc apply -f "${SCRIPT_DIR}/deployment.yaml"

echo "[deploy] Setting image ${IMAGE}"
oc set image "deployment/${APP_NAME}" "${APP_NAME}=${IMAGE}" --record=true

echo "[deploy] Image updated; rollout is triggered by deployment change"
