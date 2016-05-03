# ragnalog2

## Requirement
* Java
* Scala, sbt
* Node.js, npm
* Docker, Docker-Compose

## Setup

* start Elasticsearch and Kibana

~~~
cd docker
docker-compose up -d
~~~

* initialize mappings of Elasticsearch

~~~
cd etc/elasticsearch
./initialize.sh
~~~

* install Embulk

~~~
cd etc/embulk
./install_embulk.sh
~~~

## Build

* ragnalog-master, ragnalog-node

~~~
sbt compile
~~~

* ragnalog-ui

~~~
cd ragnalog-ui
npm install
npm run build
~~~

## Run

* start ragnalog-node

~~~
sbt node/run
~~~

* start ragnalog-master

~~~
sbt master/run
~~~

* If you want to a custom configuration file, you can use `-Dconfig.file` option.

~~~
sbt -Dconfig.file=my.conf master/run
~~~

* start dev-server for ragnalog-ui

~~~
cd ragnalog-ui
npm start
~~~

