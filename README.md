# Running
1. `$ confluent local services start` 
2. navigate to **scripts** directory and run `$ ./create-topics.sh`
3. then run `$ ./produce-tables-datagen-scores.sh`
4. finally, run the streams application 

## Features
- automatic generation of mock data for score-events, compatible with data to be found in player's `KTable<Long,Player>` and product's `GlobalKTable<Long, Product>`
- unit testing with `TopologyTestDriver` **to be cntd...*
- stream/ktable join
- stream/globalKTable join
- Avro(SOURCE)+Json(SINK)
- Materialized ReadOnly state stores
- api for querying state stores
