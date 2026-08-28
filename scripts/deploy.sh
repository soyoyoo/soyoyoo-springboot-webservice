#!/usr/bin/env bash

REPOSITORY=/home/ec2-user/app/step3
PROJECT_NAME=gettingstarted

echo "> 현재 쉬고 있는 profile 확인"
# profile.sh를 실행하여 현재 Nginx가 바라보지 않는 쉬고 있는 프로필(real1 또는 real2)을 가져옴
IDLE_PROFILE=$(bash $REPOSITORY/profile.sh)

echo "> $IDLE_PROFILE 은(는) 쉬고 있는 프로필입니다. 해당 프로필을 구동합니다."
# 쉬고 있는 프로필의 구동 중인 앱이 있다면 종료 (stop.sh)
bash $REPOSITORY/stop.sh

echo "> 5초 대기..."
sleep 5

echo "> $IDLE_PROFILE 배포 (start.sh 실행)"
# 새 JAR 파일로 쉬고 있던 프로필(port 8081 또는 8082) 앱 실행 (start.sh)
bash $REPOSITORY/start.sh

echo "> $IDLE_PROFILE 헬스 체크 시작"
echo "> curl -s http://localhost:$IDLE_PORT/profile "
# 새 앱이 정상적으로 떴는지 헬스체크 (health.sh)
bash $REPOSITORY/health.sh

echo "> Nginx 스위칭 (switch.sh 실행)"
# 정상 작동 확인 후 Nginx가 바라보는 포트를 새 앱으로 전환 및 Nginx reload (switch.sh)
bash $REPOSITORY/switch.sh