#!/bin/bash
# deploy.sh — đặt trên VPS tại /opt/myapp/deploy.sh

set -e  # dừng ngay nếu có lệnh nào lỗi

APP_DIR="/opt/apps/myapp"
SERVICE_NAME="java-spring-boot"
JAR_PATTERN="build/libs/*.jar"

echo "==> [1/4] Pulling latest code..."
cd $APP_DIR
git pull origin master

echo "==> [2/4] Building JAR..."
./gradlew bootJar -x test --no-daemon --quiet

echo "==> [3/4] Restarting service..."
sudo systemctl restart $SERVICE_NAME

echo "==> [4/4] Waiting for app to start..."
sleep 10

# Kiểm tra app đã lên chưa
if sudo systemctl is-active --quiet $SERVICE_NAME; then
    echo "✓ Deploy successful! App is running."
    sudo journalctl -u $SERVICE_NAME -n 20 --no-pager
else
    echo "✗ Deploy FAILED! Check logs:"
    sudo journalctl -u $SERVICE_NAME -n 50 --no-pager
    exit 1
fi
