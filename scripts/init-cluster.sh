#!/bin/bash

NODES="$1"

echo "Initializing a cluster with $NODES nodes"

if [ $1 -eq 1 ]
then
  gcloud dataproc clusters create cpa-cluster --enable-component-gateway \
    --region europe-west2 --subnet default --no-address \
    --master-machine-type n4-standard-2 --master-boot-disk-type hyperdisk-balanced \
    --master-boot-disk-size 200 --worker-machine-type n4-standard-2 \
    --worker-boot-disk-type hyperdisk-balanced --worker-boot-disk-size 200 \
    --image-version 2.2-debian12 --properties spark:spark.dataproc.enhanced.optimizer.enabled=true,spark:spark.dataproc.enhanced.execution.enabled=true \
    --max-idle 3600s --project careful-mapper-461409-d1 --single-node
fi

gcloud dataproc clusters create cpa-cluster --enable-component-gateway \
  --region europe-west2 --subnet default --no-address \
  --master-machine-type n4-standard-2 --master-boot-disk-type hyperdisk-balanced \
  --master-boot-disk-size 200 --num-workers 2 --worker-machine-type n4-standard-2 \
  --worker-boot-disk-type hyperdisk-balanced --worker-boot-disk-size 200 \
  --image-version 2.2-debian12 --properties spark:spark.dataproc.enhanced.optimizer.enabled=true,spark:spark.dataproc.enhanced.execution.enabled=true \
  --max-idle 3600s --project careful-mapper-461409-d1
