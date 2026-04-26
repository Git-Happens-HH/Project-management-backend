#!/usr/bin/env bash
set -euo pipefail

if [ "$#" -lt 2 ]; then
  echo "Usage: $0 <namespace> <app-name> [route-host]"
  exit 1
fi

NAMESPACE="$1"
APP_NAME="$2"
ROUTE_HOST="${3:-}"

oc project "${NAMESPACE}"

echo "[verify] Waiting for rollout to complete"
oc rollout status "deployment/${APP_NAME}" --timeout=300s

echo "[verify] Checking pod readiness"
oc get pods -l "app=${APP_NAME}" -o wide

# Prefer explicit host if provided; otherwise resolve from OpenShift route data.
if [ -z "${ROUTE_HOST}" ]; then
  ROUTE_HOST="$(oc get route "${APP_NAME}" -o jsonpath='{.spec.host}' 2>/dev/null || true)"
fi

if [ -z "${ROUTE_HOST}" ]; then
  ROUTE_HOST="$(oc get route -l "app=${APP_NAME}" -o jsonpath='{.items[0].spec.host}' 2>/dev/null || true)"
fi

if [ -n "${ROUTE_HOST}" ]; then
  echo "[verify] Running health check against https://${ROUTE_HOST}/actuator/health"
  curl --fail --silent --show-error --max-time 20 "https://${ROUTE_HOST}/actuator/health" >/dev/null
  echo "[verify] Health check passed"
else
  echo "[verify] Route not found; skipped external health check"
fi
