#!/bin/sh

mvn clean package docker:build

# Removing dangling images
docker rmi $(docker images --quiet --filter "dangling=true") -f