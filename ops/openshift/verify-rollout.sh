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
if ! oc rollout status "deployment/${APP_NAME}" --timeout=600s; then
  echo "[verify] Rollout failed or timed out; collecting diagnostics"
  oc get deployment "${APP_NAME}" -o wide || true
  oc describe deployment "${APP_NAME}" || true
  oc get rs -l "app=${APP_NAME}" -o wide || true
  oc get pods -l "app=${APP_NAME}" -o wide || true
  oc describe pods -l "app=${APP_NAME}" || true
  echo "[verify] Recent namespace events"
  oc get events --sort-by=.metadata.creationTimestamp | tail -n 30 || true
  exit 1
fi

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
  HEALTH_CHECK_MAX_ATTEMPTS="${HEALTH_CHECK_MAX_ATTEMPTS:-12}"
  HEALTH_CHECK_SLEEP_SECONDS="${HEALTH_CHECK_SLEEP_SECONDS:-10}"
  HEALTH_CHECK_TIMEOUT_SECONDS="${HEALTH_CHECK_TIMEOUT_SECONDS:-20}"

  echo "[verify] Running health check against https://${ROUTE_HOST}/actuator/health"

  attempt=1
  while [ "${attempt}" -le "${HEALTH_CHECK_MAX_ATTEMPTS}" ]; do
    if curl --fail --silent --show-error --max-time "${HEALTH_CHECK_TIMEOUT_SECONDS}" "https://${ROUTE_HOST}/actuator/health" >/dev/null; then
      echo "[verify] Health check passed"
      exit 0
    fi

    echo "[verify] Health check attempt ${attempt}/${HEALTH_CHECK_MAX_ATTEMPTS} failed; retrying in ${HEALTH_CHECK_SLEEP_SECONDS}s"
    attempt=$((attempt + 1))
    sleep "${HEALTH_CHECK_SLEEP_SECONDS}"
  done

  echo "[verify] Health check failed after ${HEALTH_CHECK_MAX_ATTEMPTS} attempts"
  oc get pods -l "app=${APP_NAME}" -o wide || true
  oc get route "${APP_NAME}" -o wide || true
  exit 1
else
  echo "[verify] Route not found; skipped external health check"
fi
