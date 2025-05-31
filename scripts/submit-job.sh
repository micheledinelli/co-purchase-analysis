#!/bin/bash

gcloud dataproc jobs submit spark \
  --cluster=cpa \
  --region=us-central1 \
  --jar=gs://cpa-scp-bucket/co-purchase-analysis_2.12-0.0.1.jar
