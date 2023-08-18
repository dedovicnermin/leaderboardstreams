package io.nermdev.kafka.leaderboardstreams;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.nermdev.kafka.leaderboardstreams.models.json.Leaderboard;
import io.nermdev.kafka.leaderboardstreams.models.json.LeaderboardHistoric;
import io.nermdev.kafka.leaderboardstreams.utils.IgnoreSchemaPropertyMixinConfig;
import io.nermdev.schemas.avro.leaderboards.ScoreCard;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyQueryMetadata;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsMetadata;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.Closeable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LeaderboardService implements Closeable {
    static final Logger log = LoggerFactory.getLogger(LeaderboardService.class);
    static final String LEADERBOARDS_STATE_STORE_TOP_10_SCORES_GLOBAL = "leaderboards-state-store_top10_scores_global";
    static final String LEADERBOARDS_STATE_STORE_TOP_10_HISTORIC_SCORES_GLOBAL = "leaderboards-state-store_top10_historic_scores_global";
    static final String LEADERBOARDS_STATE_STORE_TOP_10_SCORES_PRODUCT = "leaderboards-state-store_top10_scores_product";
    static final String LEADERBOARDS_STATE_STORE_TOP_10_HISTORIC_SCORES_PRODUCT = "leaderboards-state-store_top10_historic_scores_product";
    static final String LEADERBOARDS_STATE_STORE_PPID_HISTORIC = "leaderboards-state-store_ppid_historic";

    private final HostInfo hostInfo;
    private final KafkaStreams kafkaStreams;
    private final Javalin app;

    final ObjectMapper mapper = new ObjectMapper();


    public LeaderboardService(final HostInfo hostInfo, final KafkaStreams kafkaStreams) {
        this.hostInfo = hostInfo;
        this.kafkaStreams = kafkaStreams;
        mapper.addMixIn(SpecificRecord.class, IgnoreSchemaPropertyMixinConfig.class);
        this.app = Javalin.create().start(hostInfo.port());
    }



    private ReadOnlyKeyValueStore<Long, Leaderboard> getPlayerLeaderboardsStore() {
        return kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        LEADERBOARDS_STATE_STORE_TOP_10_SCORES_GLOBAL,
                        QueryableStoreTypes.keyValueStore()
                )
        );
    }

    private ReadOnlyKeyValueStore<Long, Leaderboard> getProductLeaderboardsStore() {
        return kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        LEADERBOARDS_STATE_STORE_TOP_10_SCORES_PRODUCT,
                        QueryableStoreTypes.keyValueStore()
                )
        );
    }

    private ReadOnlyKeyValueStore<Long, LeaderboardHistoric> getHistoricPlayerLeaderboardsStore() {
        return kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        LEADERBOARDS_STATE_STORE_TOP_10_HISTORIC_SCORES_GLOBAL,
                        QueryableStoreTypes.keyValueStore()
                )
        );
    }

    private ReadOnlyKeyValueStore<Long, LeaderboardHistoric> getHistoricProductLeaderboardsStore() {
        return kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        LEADERBOARDS_STATE_STORE_TOP_10_HISTORIC_SCORES_PRODUCT,
                        QueryableStoreTypes.keyValueStore()
                )
        );
    }

    private ReadOnlyKeyValueStore<Long, Leaderboard> getStore(final LeaderboardType leaderboardType) {
        switch (leaderboardType) {
            case PLAYER:
                return getPlayerLeaderboardsStore();
            case PRODUCT:
                return getProductLeaderboardsStore();
            default:
                throw new IllegalArgumentException("leaderboardType is invalid");

        }
    }

    private ReadOnlyKeyValueStore<Long, LeaderboardHistoric> getStoreHistoric(final LeaderboardType leaderboardType) {
        switch (leaderboardType) {
            case PLAYER_HISTORIC:
                return getHistoricPlayerLeaderboardsStore();
            case PRODUCT_HISTORIC:
                return getHistoricProductLeaderboardsStore();
            default:
                throw new IllegalArgumentException("leaderboardType is invalid");

        }
    }


    void start() {
        app.get("/leaderboard/players", this::getPlayerLeaderboards);
        app.get("/leaderboard/players/{player_id}", this::getPlayerLeaderboard);
        app.get("/leaderboard/players/count", this::getPlayerLeaderboardsCount);
        app.get("/leaderboard/players/count/local", this::getPlayerLeaderboardsCountLocal);
        app.get("/leaderboard/players/{player_id_start}/{player_id_end}", this::getPlayerLeaderboardRange);

        app.get("/leaderboard/products", this::getProductLeaderboards);
        app.get("/leaderboard/products/{product_id}", this::getProductLeaderboard);
        app.get("/leaderboard/products/count", this::getProductLeaderboardsCount);
        app.get("/leaderboard/products/count/local", this::getProductLeaderboardsCountLocal);
        app.get("/leaderboard/products/{product_id_start}/{product_id_end}", this::getProductLeaderboardRange);

        app.get("/leaderboard/historic/players/{player_id}", this::getPlayerHistoricLeaderboard);
        app.get("/leaderboard/historic/products/{product_id}", this::getProductHistoricLeaderboard);

    }




    /** Global key-value store query: approximate number of entries */
    private void getPlayerLeaderboardsCount(final Context context) {
        long count = getPlayerLeaderboardsStore().approximateNumEntries();
        for (StreamsMetadata streamsMetadata : kafkaStreams.metadataForAllStreamsClients()) {
            if (!hostInfo.equals(streamsMetadata.hostInfo())) continue;
            count += fetchPlayerLeaderboardsCountFromRemoteInstance(streamsMetadata.hostInfo().host(), streamsMetadata.hostInfo().port());
        }
        context.json(count);

    }

    /** Local key-value store query: approximate number of entries */
    private void getPlayerLeaderboardsCountLocal(Context ctx) {
        long count = 0L;
        try {
            count = getPlayerLeaderboardsStore().approximateNumEntries();
        } catch (Exception e) {
            log.error("Could not get local player leaderboard count --- {}", e.getMessage());
        } finally {
            ctx.result(String.valueOf(count));
        }
    }


    private void getProductLeaderboardsCount(final Context ctx) {
        long count = getProductLeaderboardsStore().approximateNumEntries();
        for (StreamsMetadata streamsMetadata : kafkaStreams.metadataForAllStreamsClients()) {
            if (!hostInfo.equals(streamsMetadata.hostInfo())) continue;

            count += fetchProductLeaderboardsCountFromRemoteInstance(streamsMetadata.hostInfo().host(), streamsMetadata.hostInfo().port());
        }
        ctx.json(count);
    }

    private void getProductLeaderboardsCountLocal(final Context ctx) {
        long count = 0L;
        try {
            count = getProductLeaderboardsStore().approximateNumEntries();
        } catch (Exception e) {
            log.error("Could not get local product leaderboard count --- {}", e.getMessage());
            e.printStackTrace();
        } finally {
            ctx.result(String.valueOf(count));
        }
    }



    /** Local K,V store query - return all entries */
    @SneakyThrows
    private void getPlayerLeaderboards(final Context context) {
        final Map<Long, List<ScoreCard>> leaderboard = new HashMap<>();
        try (final KeyValueIterator<Long, Leaderboard> stateStoreRecordIterator = getPlayerLeaderboardsStore().all()) {
            while (stateStoreRecordIterator.hasNext()) {
                final KeyValue<Long, Leaderboard> record = stateStoreRecordIterator.next();
                leaderboard.put(record.key,record.value.toList());
            }
        }
        final String json = mapper.writeValueAsString(leaderboard);
        context.json(json);
    }

    @SneakyThrows
    private void getProductLeaderboards(final Context context) {
        final Map<Long, List<ScoreCard>> leaderboard = new HashMap<>();
        try (final KeyValueIterator<Long, Leaderboard> stateStoreRecordIterator = getProductLeaderboardsStore().all()) {
            while (stateStoreRecordIterator.hasNext()) {
                final KeyValue<Long, Leaderboard> record = stateStoreRecordIterator.next();
                leaderboard.put(record.key,record.value.toList());
            }
        }
        final String json = mapper.writeValueAsString(leaderboard);
        context.json(json);
    }



    /** Local key-value store query: range scan (inclusive) */
    @SneakyThrows
    void getPlayerLeaderboardRange(Context ctx) {
        final Long from = Long.valueOf(ctx.pathParam("player_id_start"));
        final Long to = Long.valueOf(ctx.pathParam("player_id_end"));

        final Map<Long, List<ScoreCard>> leaderboard = new HashMap<>();

        KeyValueIterator<Long, Leaderboard> range = getPlayerLeaderboardsStore().range(from, to);
        while (range.hasNext()) {
            KeyValue<Long, Leaderboard> next = range.next();
            leaderboard.put(next.key, next.value.toList());
        }
        // close the iterator to avoid memory leaks!
        range.close();
        // return a JSON response
        ctx.json(mapper.writeValueAsString(leaderboard));
    }

    /** Local key-value store query: range scan (inclusive) */
    @SneakyThrows
    void getProductLeaderboardRange(Context ctx) {
        final Long from = Long.valueOf(ctx.pathParam("product_id_start"));
        final Long to = Long.valueOf(ctx.pathParam("product_id_end"));

        final Map<Long, List<ScoreCard>> leaderboard = new HashMap<>();

        final KeyValueIterator<Long, Leaderboard> range = getProductLeaderboardsStore().range(from, to);
        while (range.hasNext()) {
            KeyValue<Long, Leaderboard> next = range.next();
            leaderboard.put(next.key, next.value.toList());
        }
        // close the iterator to avoid memory leaks!
        range.close();
        // return a JSON response
        ctx.json(mapper.writeValueAsString(leaderboard));
    }


    private long fetchPlayerLeaderboardsCountFromRemoteInstance(String host, int port) {
        OkHttpClient client = new OkHttpClient();

        String url = String.format("http://%s:%d/leaderboard/players/count/local", host, port);
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return Long.parseLong(Objects.requireNonNull(response.body()).string());
        } catch (Exception e) {
            // log error
            log.error("Could not get player leaderboard count --- {}", e.getMessage());
            return 0L;
        }
    }

    private long fetchProductLeaderboardsCountFromRemoteInstance(String host, int port) {
        OkHttpClient client = new OkHttpClient();

        String url = String.format("http://%s:%d/leaderboard/products/count/local", host, port);
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return Long.parseLong(Objects.requireNonNull(response.body()).string());
        } catch (Exception e) {
            // log error
            log.error("Could not get product leaderboard count --- {}", e.getMessage());
        }
        return 0L;

    }


    @SneakyThrows
    void getPlayerLeaderboard(Context ctx) {
        Long playerId = Long.valueOf(ctx.pathParam("player_id"));
        getLeaderboard(ctx, playerId, LeaderboardType.PLAYER);


    }


    private void getProductLeaderboard(final Context ctx) {
        final Long productId = Long.valueOf(ctx.pathParam("product_id"));
        getLeaderboard(ctx, productId, LeaderboardType.PRODUCT);


    }

    private void getPlayerHistoricLeaderboard(final Context ctx) {
        Long playerId = Long.valueOf(ctx.pathParam("player_id"));
        getLeaderboard(ctx, playerId, LeaderboardType.PLAYER_HISTORIC);
    }

    private void getProductHistoricLeaderboard(final Context ctx) {
        final Long productId = Long.valueOf(ctx.pathParam("product_id"));
        getLeaderboard(ctx, productId,  LeaderboardType.PRODUCT_HISTORIC);
    }



    @SneakyThrows
    private void getLeaderboard(final Context ctx, final Long id, final LeaderboardType leaderboardType) {
        final KeyQueryMetadata metadata = kafkaStreams.queryMetadataForKey(leaderboardType.getStateStoreName(), id, Serdes.Long().serializer());
        // the local instance has this key
        if (hostInfo.equals(metadata.activeHost())) {

            log.info("Querying local store for key");
            if (leaderboardType == LeaderboardType.PLAYER_HISTORIC || leaderboardType == LeaderboardType.PRODUCT_HISTORIC) {
                LeaderboardHistoric highScores = getStoreHistoric(leaderboardType).get(id);
                if (Objects.isNull(highScores)) {
                    //  was not found
                    ctx.status(404);
                    return;
                }

                // player was found, so return the high scores
                ctx.json(mapper.writeValueAsString(highScores.toList()));
            } else {
                Leaderboard highScores = getStore(leaderboardType).get(id);

                if (Objects.isNull(highScores)) {
                    //  was not found
                    ctx.status(404);
                    return;
                }

                // player was found, so return the high scores
                ctx.json(mapper.writeValueAsString(highScores.toList()));
            }
            return;

        }

        // a remote instance has the key
        final String remoteHost = metadata.activeHost().host();
        final int remotePort = metadata.activeHost().port();
        final String url = String.format(
                "http://%s:%d/leaderboard" + leaderboardType.getUrlSuffix(),
                // params
                remoteHost,
                remotePort,
                id
        );

        // issue the request
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            log.info("Querying remote store for key");
            ctx.result(Objects.requireNonNull(response.body()).string());
        } catch (Exception e) {
            ctx.status(500);
        }


    }



    @Override
    public void close()  {
        app.close();
    }
}
