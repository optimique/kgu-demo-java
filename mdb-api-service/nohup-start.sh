#!/usr/bin/env sh

nohup java -jar build/libs/mdb-api-service-0.9-SNAPSHOT.jar & echo $! > nohup-run.pid
