#!/usr/bin/env sh

nohup java -jar build/libs/mdb-api-service-0.9.1-SNAPSHOT.jar & echo $! > nohup-run.pid
