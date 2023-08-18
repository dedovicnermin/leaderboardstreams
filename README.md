
# Running
TODO

## Features

- unit testing with `TopologyTestDriver` **to be cntd...*
[//]: # (- stream/ktable join)
[//]: # (- stream/globalKTable join)
[//]: # (- Avro&#40;SOURCE&#41;+Json&#40;SINK&#41;)
- Materialized ReadOnly state stores
- api for querying state stores 
  - `curl localhost:7000/leaderboard/<players | products> | jq .`
  - `curl localhost:7000/leaderboard/<players | products>/count`
  - `curl localhost:7000/leaderboard/<players | products>/count/local` (partitions must be > 1 in order for this to be effective)
  - `curl localhost:7000/leaderboard/<players | products>/100/500 | jq .` (range of product ID's)
  - `curl localhost:7000/leaderboard/<players | products>/{{id}}` (query one leaderboard)
  - `curl localhost:7000/leaderboard/historic/<players | products>/{{id}}`

### View Topology
1. [Go to Topology visualizer tool](https://zz85.github.io/kafka-streams-viz/)
2. paste the following topology (described and printed on start up):
```
Topologies:
   Sub-topology: 0
    Source: KSTREAM-SOURCE-0000000000 (topics: [leaderboard.scorecards])
      --> KSTREAM-MAPVALUES-0000000001
    Processor: KSTREAM-MAPVALUES-0000000001 (stores: [])
      --> KSTREAM-AGGREGATE-0000000002, KSTREAM-KEY-SELECT-0000000004, lbs-selectKey-playerId-to-ppid
      <-- KSTREAM-SOURCE-0000000000
    Processor: lbs-selectKey-playerId-to-ppid (stores: [])
      --> KSTREAM-MAPVALUES-0000000011
      <-- KSTREAM-MAPVALUES-0000000001
    Processor: KSTREAM-AGGREGATE-0000000002 (stores: [leaderboards-state-store_top10_scores_global])
      --> agg001
      <-- KSTREAM-MAPVALUES-0000000001
    Processor: KSTREAM-KEY-SELECT-0000000004 (stores: [])
      --> leaderboards-state-store_top10_scores_product-repartition-filter
      <-- KSTREAM-MAPVALUES-0000000001
    Processor: KSTREAM-MAPVALUES-0000000011 (stores: [])
      --> lbs-groupby-ppid-repartition-filter
      <-- lbs-selectKey-playerId-to-ppid
    Processor: agg001 (stores: [])
      --> test.sink
      <-- KSTREAM-AGGREGATE-0000000002
    Processor: lbs-groupby-ppid-repartition-filter (stores: [])
      --> lbs-groupby-ppid-repartition-sink
      <-- KSTREAM-MAPVALUES-0000000011
    Processor: leaderboards-state-store_top10_scores_product-repartition-filter (stores: [])
      --> leaderboards-state-store_top10_scores_product-repartition-sink
      <-- KSTREAM-KEY-SELECT-0000000004
    Sink: lbs-groupby-ppid-repartition-sink (topic: lbs-groupby-ppid-repartition)
      <-- lbs-groupby-ppid-repartition-filter
    Sink: leaderboards-state-store_top10_scores_product-repartition-sink (topic: leaderboards-state-store_top10_scores_product-repartition)
      <-- leaderboards-state-store_top10_scores_product-repartition-filter
    Sink: test.sink (topic: outbound.test)
      <-- agg001

  Sub-topology: 1
    Source: leaderboards-state-store_top10_scores_product-repartition-source (topics: [leaderboards-state-store_top10_scores_product-repartition])
      --> KSTREAM-AGGREGATE-0000000005
    Processor: KSTREAM-AGGREGATE-0000000005 (stores: [leaderboards-state-store_top10_scores_product])
      --> agg002
      <-- leaderboards-state-store_top10_scores_product-repartition-source
    Processor: agg002 (stores: [])
      --> none
      <-- KSTREAM-AGGREGATE-0000000005

  Sub-topology: 2
    Source: lbs-groupby-ppid-repartition-source (topics: [lbs-groupby-ppid-repartition])
      --> KSTREAM-REDUCE-0000000012
    Processor: KSTREAM-REDUCE-0000000012 (stores: [leaderboards-state-store_ppid_historic])
      --> KTABLE-TOSTREAM-0000000016, KTABLE-TOSTREAM-0000000022
      <-- lbs-groupby-ppid-repartition-source
    Processor: KTABLE-TOSTREAM-0000000016 (stores: [])
      --> lbs-selectkey-ppid-to-playerId
      <-- KSTREAM-REDUCE-0000000012
    Processor: KTABLE-TOSTREAM-0000000022 (stores: [])
      --> lbs-selectkey-ppid-to-productId
      <-- KSTREAM-REDUCE-0000000012
    Processor: lbs-selectkey-ppid-to-playerId (stores: [])
      --> lbs-groupbykey-post-ppid-to-playerid-repartition-filter
      <-- KTABLE-TOSTREAM-0000000016
    Processor: lbs-selectkey-ppid-to-productId (stores: [])
      --> lbs-groupbykey-post-ppid-to-productid-repartition-filter
      <-- KTABLE-TOSTREAM-0000000022
    Processor: lbs-groupbykey-post-ppid-to-playerid-repartition-filter (stores: [])
      --> lbs-groupbykey-post-ppid-to-playerid-repartition-sink
      <-- lbs-selectkey-ppid-to-playerId
    Processor: lbs-groupbykey-post-ppid-to-productid-repartition-filter (stores: [])
      --> lbs-groupbykey-post-ppid-to-productid-repartition-sink
      <-- lbs-selectkey-ppid-to-productId
    Sink: lbs-groupbykey-post-ppid-to-playerid-repartition-sink (topic: lbs-groupbykey-post-ppid-to-playerid-repartition)
      <-- lbs-groupbykey-post-ppid-to-playerid-repartition-filter
    Sink: lbs-groupbykey-post-ppid-to-productid-repartition-sink (topic: lbs-groupbykey-post-ppid-to-productid-repartition)
      <-- lbs-groupbykey-post-ppid-to-productid-repartition-filter

  Sub-topology: 3
    Source: lbs-groupbykey-post-ppid-to-playerid-repartition-source (topics: [lbs-groupbykey-post-ppid-to-playerid-repartition])
      --> KSTREAM-AGGREGATE-0000000018
    Processor: KSTREAM-AGGREGATE-0000000018 (stores: [leaderboards-state-store_top10_historic_scores_global])
      --> none
      <-- lbs-groupbykey-post-ppid-to-playerid-repartition-source

  Sub-topology: 4
    Source: lbs-groupbykey-post-ppid-to-productid-repartition-source (topics: [lbs-groupbykey-post-ppid-to-productid-repartition])
      --> KSTREAM-AGGREGATE-0000000024
    Processor: KSTREAM-AGGREGATE-0000000024 (stores: [leaderboards-state-store_top10_historic_scores_product])
      --> none
      <-- lbs-groupbykey-post-ppid-to-productid-repartition-source
```