#!/bin/bash

# TODO: use bundler

rm -rf ./plugins/

./bin/embulk gem install embulk-parser-grok -i ./plugins
./bin/embulk gem install embulk-output-elasticsearch -i ./plugins
# ./bin/embulk gem install embulk-filter-insert -i ./plugins
