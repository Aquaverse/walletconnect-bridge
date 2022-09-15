#!/bin/bash

# parse optional arg registry: '-r'
# Usage: sh docker_deploy.sh -r register.com:port
while getopts ":r:" opt
do
  case $opt in
    r)
      DOCKER_PUSH_REGISTRY=$OPTARG
      ;;
    ?)
      echo "invalid arg"
      exit 1
      ;;
  esac
done

IMAGE_NAME=walletconnect/bridge-server-java
IMAGE_TAG=0.2
DOCKER_PUSH_REGISTRY=${DOCKER_PUSH_REGISTRY:-maven-wh.niub.la:8482}

echo "Building ${IMAGE_NAME}:${IMAGE_TAG}"
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} \
  --build-arg HTTP_PROXY=http://10.1.5.75:1080 \
  --build-arg HTTPS_PROXY=http://10.1.5.75:1080 \
  --build-arg NO_PROXY=172.17.8.23 \
  --build-arg YARN_REPO=http://172.17.8.23:4873 \
  .
echo "Build docker image ${IMAGE_NAME}:${IMAGE_TAG} done."

#docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${DOCKER_PUSH_REGISTRY}/${IMAGE_NAME}:latest
docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${DOCKER_PUSH_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
echo "Push docker images to ${DOCKER_PUSH_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}"
#docker push ${DOCKER_PUSH_REGISTRY}/${IMAGE_NAME}:latest
docker push ${DOCKER_PUSH_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
echo "Push docker images done."