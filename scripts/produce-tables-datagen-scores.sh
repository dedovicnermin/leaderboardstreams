
kafka-avro-console-producer \
  --bootstrap-server kafka:9092 \
  --topic product-events \
  --property 'schema.registry.url=http://schema-registry:8081' \
  --property 'parse.key=true' \
  --property 'value.schema={"type":"record","namespace":"tech.nermindedovic.leaderboardstreams.models.avro","name":"Product","fields":[{"name":"id","type":"long"},{"name":"name","type":"string"}]}' \
  --property 'key.serializer=org.apache.kafka.common.serialization.LongSerializer' \
  --property 'key.separator=|' < ./products.json



kafka-avro-console-producer \
  --bootstrap-server kafka:9092 \
  --topic player-events \
  --property 'schema.registry.url=http://schema-registry:8081' \
  --property 'parse.key=true' \
  --property 'value.schema={"type":"record","namespace":"tech.nermindedovic.leaderboardstreams.models.avro","name":"Player","fields":[{"name":"id","type":"long"},{"name":"name","type":"string"},{"name":"DOB","type":"string"}]}' \
  --property 'key.serializer=org.apache.kafka.common.serialization.LongSerializer' \
  --property 'key.separator=|' < ./players.json


curl -X POST -H "Content-Type: application/json" --data "$(< ../src/main/datagen/scoreevents-gen-config.json)" connect:8083/connectors


