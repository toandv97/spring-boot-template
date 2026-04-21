#!/bin/bash

./gradlew bootJar -x test

IMAGE="toandv15/myapp"
TAG="${1:-latest}"

# Build Docker image với tag version
docker build -t $IMAGE:TAG .
docker build -t $IMAGE:latest .

# Push lên registry
docker push $IMAGE:TAG
docker push $IMAGE:latest
