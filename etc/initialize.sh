#!/bin/sh

ES_HOST=http://localhost:9200
INDEX_NAME=.ragnalog2

curl -XPUT ${ES_HOST}/_template/template_all/ -d @template_all.json
curl -XDELETE ${ES_HOST}/${INDEX_NAME}
curl -XPUT ${ES_HOST}/${INDEX_NAME} -d @create_mapping.json
