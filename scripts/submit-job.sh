#!/bin/bash

# Usage: ./submit-job.sh <CLUSTER>
# Requires: BUCKET and REGION env vars to be set.

if [ "$#" -lt 1 ]; then
  echo "Usage: $0 <CLUSTER>"
  exit 1
fi

echo "Submitting a Job to cluster $1/$REGION. Using $BUCKET as input and .jar source"

RANDOM_STRING=$(openssl rand -hex 12)

gcloud dataproc jobs submit spark \
  --cluster="$1" \
  --region="$REGION" \
  --jar=gs://"$BUCKET"/co-purchase-analysis_2.12-1.0.0.jar \
  -- "gs://$BUCKET/order_products.csv" "gs://$BUCKET/out-$1-$2-$RANDOM_STRING"

