#!/bin/bash
# /opt/myapp/deploy.sh
# Ưu điểm lớn nhất: Rollback cực nhanh vì image cũ vẫn còn trong registry.
# VPS không cần JDK hay Gradle — cực kỳ nhẹ.

set -e

IMAGE="toandv15/myapp"
TAG="${1:-latest}"          # nhận version qua argument, mặc định là latest
CONTAINER_NAME="myapp"
PORT="9000"

echo "==> [1/4] Pulling image $IMAGE:$TAG ..."
docker pull $IMAGE:$TAG

echo "==> [2/4] Stopping old container (if running)..."
docker stop $CONTAINER_NAME 2>/dev/null || true
docker rm   $CONTAINER_NAME 2>/dev/null || true

echo "==> [3/4] Starting new container..."
docker run -d \
  --name $CONTAINER_NAME \
  --restart unless-stopped \
  --env-file /opt/apps/myapp.env \
  -p $PORT:8080 \
  $IMAGE:$TAG

echo "==> [4/4] Checking health..."
sleep 8
if docker ps | grep -q $CONTAINER_NAME; then
    echo "✓ Container is running!"
    docker logs $CONTAINER_NAME --tail 30
else
    echo "✗ Container failed to start!"
    docker logs $CONTAINER_NAME
    exit 1
fi