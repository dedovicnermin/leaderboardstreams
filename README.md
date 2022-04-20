# Running
1. `$ confluent local services start` 
2. navigate to **scripts** directory and run `$ ./create-topics.sh`
3. run the streams application
4. then run `$ ./produce-tables-datagen-scores.sh`

## Features
- automatic generation of mock data for score-events, compatible with data to be found in player's `KTable<Long,Player>` and product's `GlobalKTable<Long, Product>`
- unit testing with `TopologyTestDriver` **to be cntd...*
- stream/ktable join
- stream/globalKTable join
- Avro(SOURCE)+Json(SINK)
- Materialized ReadOnly state stores
- api for querying state stores **to be cntd...*

### View Topology
1. [Go to Topology visualizer tool](https://zz85.github.io/kafka-streams-viz/)
2. paste the following topology (described and printed on start up):
   ```text
   Topologies:
   Sub-topology: 0
    Source: KSTREAM-SOURCE-0000000000 (topics: [score-events])
      --> KSTREAM-KEY-SELECT-0000000001
    Processor: KSTREAM-KEY-SELECT-0000000001 (stores: [])
      --> KSTREAM-FILTER-0000000009
      <-- KSTREAM-SOURCE-0000000000
    Processor: KSTREAM-FILTER-0000000009 (stores: [])
      --> KSTREAM-SINK-0000000008
      <-- KSTREAM-KEY-SELECT-0000000001
    Sink: KSTREAM-SINK-0000000008 (topic: KSTREAM-KEY-SELECT-0000000001-repartition)
      <-- KSTREAM-FILTER-0000000009

    Sub-topology: 1
   Source: KSTREAM-SOURCE-0000000010 (topics: [KSTREAM-KEY-SELECT-0000000001-repartition])
   --> KSTREAM-LEFTJOIN-0000000011
   Processor: KSTREAM-LEFTJOIN-0000000011 (stores: [player-events-STATE-STORE-0000000002])
   --> KSTREAM-KEY-SELECT-0000000012
   <-- KSTREAM-SOURCE-0000000010
   Processor: KSTREAM-KEY-SELECT-0000000012 (stores: [])
   --> KSTREAM-LEFTJOIN-0000000013
   <-- KSTREAM-LEFTJOIN-0000000011
   Processor: KSTREAM-LEFTJOIN-0000000013 (stores: [])
   --> KSTREAM-FILTER-0000000017
   <-- KSTREAM-KEY-SELECT-0000000012
   Processor: KSTREAM-FILTER-0000000017 (stores: [])
   --> KSTREAM-SINK-0000000016
   <-- KSTREAM-LEFTJOIN-0000000013
   Source: KSTREAM-SOURCE-0000000003 (topics: [player-events])
   --> KTABLE-SOURCE-0000000004
   Sink: KSTREAM-SINK-0000000016 (topic: KSTREAM-AGGREGATE-STATE-STORE-0000000014-repartition)
   <-- KSTREAM-FILTER-0000000017
   Processor: KTABLE-SOURCE-0000000004 (stores: [player-events-STATE-STORE-0000000002])
   --> none
   <-- KSTREAM-SOURCE-0000000003

   Sub-topology: 2 for global store (will not generate tasks)
   Source: KSTREAM-SOURCE-0000000006 (topics: [product-events])
   --> KTABLE-SOURCE-0000000007
   Processor: KTABLE-SOURCE-0000000007 (stores: [product-events-STATE-STORE-0000000005])
   --> none
   <-- KSTREAM-SOURCE-0000000006
   Sub-topology: 3
   Source: KSTREAM-SOURCE-0000000018 (topics: [KSTREAM-AGGREGATE-STATE-STORE-0000000014-repartition])
   --> leaderboards-state-store
   Processor: leaderboards-state-store (stores: [KSTREAM-AGGREGATE-STATE-STORE-0000000014])
   --> KTABLE-TOSTREAM-0000000019
   <-- KSTREAM-SOURCE-0000000018
   Processor: KTABLE-TOSTREAM-0000000019 (stores: [])
   --> KSTREAM-PRINTER-0000000020, KSTREAM-SINK-0000000021
   <-- leaderboards-state-store
   Processor: KSTREAM-PRINTER-0000000020 (stores: [])
   --> none
   <-- KTABLE-TOSTREAM-0000000019
   Sink: KSTREAM-SINK-0000000021 (topic: outbound-events)
   <-- KTABLE-TOSTREAM-0000000019