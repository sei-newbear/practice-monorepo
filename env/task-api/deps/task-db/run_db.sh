#!/bin/bash

docker build . -t task-db
docker run -p 5432:5432 -it --rm task-db
