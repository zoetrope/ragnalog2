#!/bin/sh

ES_HOST=http://localhost:9200

curl -XPUT ${ES_HOST}/_template/template_all/ -d @template_all.json
curl -XDELETE ${ES_HOST}/.ragnalog2
curl -XPUT ${ES_HOST}/.ragnalog2 -d @create_mapping.json
