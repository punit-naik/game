#!/bin/bash

if [ -z "$2" ]
  then
    java -jar target/game-1.0.0-standalone.jar $1
  else
    java -jar target/game-1.0.0-standalone.jar $1 $2
fi
