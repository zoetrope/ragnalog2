#!/bin/bash

rm -rf ./bin/

curl --create-dirs -o ./bin/embulk -L "http://dl.embulk.org/embulk-latest.jar"
chmod +x ./bin/embulk

