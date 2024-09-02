#!/bin/bash

PROJECT=edubill-prod-api
CONTAINER_ID=$(docker ps -a | grep "${PROJECT}" | awk '{ print $1 }')
IMAGE_ID=$(docker images | grep "${PROJECT}" | awk '{ print $3 }')

if [ -z "${CONTAINER_ID}" ]; then
  echo "> 도커 프로세스 없음으로 종료하지 않음"
else
  echo "> 도커 프로세스 확인으로 종료"
  docker stop "${CONTAINER_ID}"
  docker rm "${CONTAINER_ID}"

  if [ -z "${IMAGE_ID}" ]; then
    echo "> 도커 이미지 없음으로 삭제하지 않음"
  else
    docker rmi "${IMAGE_ID}"
  fi
fi