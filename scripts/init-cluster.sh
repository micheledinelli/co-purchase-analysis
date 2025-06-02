#!/bin/bash

# Usage: ./init-cluster.sh <NUM_NODES>
# Requires: PROJECT and REGION env vars to be set.

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <NUM_NODES>"
  exit 1
fi

NODES="$1"

if [ "$1" -eq 1 ]
then
  echo "Initializing a single node cluster $REGION for project $PROJECT"
  gcloud dataproc clusters create cpa-cluster-"$1" --region "$REGION" --master-boot-disk-size 240 --project "$PROJECT" --single-node
  exit 0
fi

echo "Initializing a cluster with $NODES nodes in $REGION for project $PROJECT"

gcloud dataproc clusters create cpa-cluster-"$1" \
  --region "$REGION" \
  --master-boot-disk-size 240 \
  --num-workers "$1" --worker-boot-disk-size 240 \
  --project "$PROJECT"
