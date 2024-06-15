#!/bin/bash
source /home/ubuntu/.bash_profile
BUILD_JAR=$(ls /home/ubuntu/dev/action/build/libs/edubillApi-0.0.1-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)

# 파일 이름 변경을 위한 접미사 추가
ENV_SUFFIX="-production"  # 또는 "-test" 등 환경에 맞게 조정
NEW_JAR_NAME="${JAR_NAME%.jar}${ENV_SUFFIX}.jar"

echo "> build 파일명: $JAR_NAME" >> /home/ubuntu/dev/action/deploy.log
echo "> 변경된 파일명: $NEW_JAR_NAME" >> /home/ubuntu/dev/action/deploy.log

DEPLOY_PATH=/home/ubuntu/dev/action/
cp $BUILD_JAR $DEPLOY_PATH$NEW_JAR_NAME

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ubuntu/dev/action/deploy.log
CURRENT_PID=$(pgrep -f $NEW_JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/dev/action/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

DEPLOY_JAR=$DEPLOY_PATH$NEW_JAR_NAME
echo "> DEPLOY_JAR 배포"    >> /home/ubuntu/dev/action/deploy.log
JAVA_OPTS="--spring.profiles.active=$SPRING_DEV_PROFILE --server.port=8081"

nohup java -jar $DEPLOY_JAR $JAVA_OPTS >> /home/ubuntu/dev/deploy.log 2>> /home/ubuntu/dev/action/deploy_err.log &

