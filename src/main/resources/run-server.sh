#!/bin/bash

SERVICE_NAME="run-server.service"

# 1. 서비스 파일을 /etc/systemd/system 경로로 강제 복사
sudo cp run-server.service /etc/systemd/system/

# 2. systemd 데몬 재시작, enable, start 명령 실행
sudo systemctl daemon-reload

# 3. 서비스 상태 확인
if sudo systemctl is-active --quiet $SERVICE_NAME; then
    echo "Stopping the existing $SERVICE_NAME..."
    sudo systemctl stop $SERVICE_NAME
fi

# 4. 서비스 시작
echo "Starting $SERVICE_NAME..."
sudo systemctl start $SERVICE_NAME
