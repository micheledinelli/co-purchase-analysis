#!/bin/bash

# Usage: ./clean.sh <CLUSTER> [--buckets]
# Requires: BUCKET and REGION env vars to be set.

if [ "$#" -lt 1 ]; then
  echo "Usage: $0 <CLUSTER> [--buckets]"
  exit 1
fi

CLUSTER="$1"
REMOVE_BUCKETS="$2"

echo "Deleting Dataproc cluster: $CLUSTER/$REGION"
gcloud dataproc clusters delete "$CLUSTER" --region="$REGION" --quiet

if [ "$REMOVE_BUCKETS" == "--buckets" ]; then
  echo "Deleting all buckets"
  gcloud storage rm -r "gs://"
fi
