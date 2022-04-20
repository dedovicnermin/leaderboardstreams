

# create the game events topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic score-events \
  --replication-factor 1 \
  --partitions 1 \
  --create

# create the players topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic player-events \
  --replication-factor 1 \
  --partitions 1 \
  --create

# create the products topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic product-events \
  --replication-factor 1 \
  --partitions 1 \
  --create

# create the high-scores topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic outbound-events \
  --replication-factor 1 \
  --partitions 1 \
  --create

exit 0;
