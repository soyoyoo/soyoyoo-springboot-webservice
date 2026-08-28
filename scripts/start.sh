#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app/step3
PROJECT_NAME=gettingstarted

echo "> Build 파일 복사"
# step2 디렉토리로 이동
cd $REPOSITORY


echo "> 새 어플리케이션 배포"
# JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | grep -v 'plain' | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

nohup java \
    -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties,classpath:/application-$IDLE_PROFILE.properties \
    -Dspring.profiles.active=real \
    -jar $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &


echo "> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."