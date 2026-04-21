#!/bin/bash
# deploy.sh — đặt trên VPS tại /opt/myapp/deploy.sh
#⚠️ Nhược điểm của cách này: Build trên server tốn RAM và CPU của production.
# Với VPS nhỏ (1GB RAM), Gradle có thể bị OOM killed

set -e  # dừng ngay nếu có lệnh nào lỗi

APP_DIR="/opt/apps/myapp"
VAR_FILE="/opt/apps/myapp.env"
SERVICE_NAME="myapp"
JAR_PATTERN="build/libs/*.jar"

echo "==> [1/5] Pulling latest code..."
cd $APP_DIR
git pull origin master

echo "==> [2/5] Setting enviroment variables"
set -a && source $VAR_FILE && set +a

echo "==> [3/5] Building JAR..."
./gradlew bootJar -x test --no-daemon --quiet
ls -lh build/libs/

echo "==> [4/5] Restarting service..."
sudo systemctl restart $SERVICE_NAME

echo "==> [5/5] Waiting for app to start..."
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
