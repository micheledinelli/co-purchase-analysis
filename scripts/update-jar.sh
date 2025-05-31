#!/bin/bash

echo "Creating JAR"

cd .. || { echo "Failed to change directory"; exit 1; }

sbt clean compile package || { echo "SBT build failed"; exit 1; }

echo "Publishing to Google Cloud bucket"

gcloud storage cp target/scala-2.12/co-purchase-analysis_2.12-0.0.1.jar \
    gs://cpa-scp-bucket/co-purchase-analysis_2.12-0.0.1.jar || { echo "gcloud upload failed"; exit 1; }

