#!/bin/bash

# docker run -it --rm -p 8089:8080 --name wiremock -v $PWD:/home/wiremock wiremock/wiremock

docker build . -t apimock
docker run -p 8089:8080 -it --rm apimock