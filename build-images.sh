#!/bin/bash

# Example: ./build-images.sh my-docker-hub-user/webapp latest linux/amd64,linux/arm64

set -e

# Tham số truyền vào
IMAGE_NAME=$1
TAG=$2
PLATFORMS=$3

# 1. Kiểm tra tham số bắt buộc
if [ -z "$IMAGE_NAME" ] || [ -z "$TAG" ]; then
    echo "Lỗi: Thiếu tham số!"
    echo "Sử dụng: $0 <image_name> <tag> [platforms]"
    echo "Ví dụ: $0 my-app v1.0 (Tự lấy kiến trúc máy này)"
    echo "Ví dụ: $0 my-app v1.0 linux/amd64,linux/arm64 (Build multi-arch)"
    exit 1
fi

# 2. Xử lý logic kiến trúc mặc định
if [ -z "$PLATFORMS" ]; then
    # Lấy kiến trúc của hệ thống hiện tại (ví dụ: linux/amd64 hoặc linux/arm64)
    ARCH=$(uname -m)
    case $ARCH in
        x86_64)  PLATFORMS="linux/amd64" ;;
        aarch64|arm64) PLATFORMS="linux/arm64" ;;
        *) PLATFORMS="linux/amd64" ;; # Mặc định nếu không xác định được
    esac
    echo "--> Không có platform được chọn. Tự động dùng kiến trúc máy này: $PLATFORMS"
else
    echo "--> Kiến trúc được chỉ định: $PLATFORMS"
fi

FULL_IMAGE_NAME="${IMAGE_NAME}:${TAG}"
BUILDER_NAME="multi-arch-builder"

# 3. Thiết lập Builder
if ! docker buildx inspect $BUILDER_NAME > /dev/null 2>&1; then
    docker buildx create --name $BUILDER_NAME --use
fi
docker buildx use $BUILDER_NAME
docker buildx inspect --bootstrap

# 4. Thực hiện Build và Push
echo "-------------------------------------------------------"
echo "Đang build: ${FULL_IMAGE_NAME} cho ${PLATFORMS}"
echo "-------------------------------------------------------"

docker buildx build \
  --platform "$PLATFORMS" \
  -t "$FULL_IMAGE_NAME" \
  --push .

if [ $? -eq 0 ]; then
    echo "Thành công!"
    docker buildx imagetools inspect "$FULL_IMAGE_NAME"
else
    echo "Build thất bại!"
    exit 1
fi
