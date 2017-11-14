#!/usr/bin/env sh

nohup java -Djava.security.egd=file:/dev/./urandom -jar build/libs/mdb-api-service-0.9.1-SNAPSHOT.jar & echo $! > nohup-run.pid
