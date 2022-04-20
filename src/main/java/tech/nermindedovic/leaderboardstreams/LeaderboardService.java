package tech.nermindedovic.leaderboardstreams;

import io.javalin.Javalin;
import io.javalin.http.Context;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyQueryMetadata;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.*;
import tech.nermindedovic.leaderboardstreams.models.json.Leaderboard;
import tech.nermindedovic.leaderboardstreams.models.json.ScorePlayerRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LeaderboardService {
    static final String LEADERBOARD_STORE_NAME = "leaderboards-state-store";
    private final HostInfo hostInfo;
    private final KafkaStreams kafkaStreams;

    public LeaderboardService(final HostInfo hostInfo, final KafkaStreams kafkaStreams) {
        this.hostInfo = hostInfo;
        this.kafkaStreams = kafkaStreams;
    }

    ReadOnlyKeyValueStore<Long, Leaderboard> getStore() {
        return kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(
                        LEADERBOARD_STORE_NAME,
                        QueryableStoreTypes.keyValueStore()
                )
        );
    }

    void start() {
        final Javalin app = Javalin.create().start(hostInfo.port());

        app.get("/leaderboard", this::getAll);
        app.get("/leaderboard/count", this::getCount);
        app.get("/leaderboard/count/local", this::getCountLocal);
        app.get("/leaderboard/:from/:to", this::getRange);
        app.get("/leaderboard/:key", this::getKey);

    }



    /** Local key-value store query: approximate number of entries */
    private void getCount(final Context context) {
        long count = getStore().approximateNumEntries();
        for (StreamsMetadata streamsMetadata : kafkaStreams.allMetadataForStore(LEADERBOARD_STORE_NAME)) {
            if (!hostInfo.equals(streamsMetadata.hostInfo())) continue;

            count += fetchCountFromRemoteInstance(streamsMetadata.hostInfo().host(), streamsMetadata.hostInfo().port());
        }
        context.json(count);
    }




    /** Local K,V store query - return all entries */
    private void getAll(final Context context) {
        final Map<Long, List<ScorePlayerRecord>> leaderboard = new HashMap<>();
        try (final KeyValueIterator<Long, Leaderboard> stateStoreRecordIterator = getStore().all()) {
            while (stateStoreRecordIterator.hasNext()) {
                final KeyValue<Long, Leaderboard> record = stateStoreRecordIterator.next();
                leaderboard.put(record.key,record.value.toList());
            }
        }
        context.json(leaderboard);
    }

    /** Local key-value store query: range scan (inclusive) */
    void getRange(Context ctx) {
        Long from = Long.valueOf(ctx.pathParam("from"));
        Long to = Long.valueOf(ctx.pathParam("to"));

        Map<Long, List<ScorePlayerRecord>> leaderboard = new HashMap<>();

        KeyValueIterator<Long, Leaderboard> range = getStore().range(from, to);
        while (range.hasNext()) {
            KeyValue<Long, Leaderboard> next = range.next();
            leaderboard.put(next.key, next.value.toList());
        }
        // close the iterator to avoid memory leaks!
        range.close();
        // return a JSON response
        ctx.json(leaderboard);
    }


    long fetchCountFromRemoteInstance(String host, int port) {
        OkHttpClient client = new OkHttpClient();

        String url = String.format("http://%s:%d/leaderboard/count/local", host, port);
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return Long.parseLong(Objects.requireNonNull(response.body()).string());
        } catch (Exception e) {
            // log error
            System.err.println("Could not get leaderboard count --- " + e);
            return 0L;
        }
    }

    /** Local key-value store query: approximate number of entries */
    void getCountLocal(Context ctx) {
        long count = 0L;
        try {
            count = getStore().approximateNumEntries();
        } catch (Exception e) {
            System.err.println("Could not get local leaderboard count --- " + e);
        } finally {
            ctx.result(String.valueOf(count));
        }
    }

    void getKey(Context ctx) {
        Long productId = Long.valueOf(ctx.pathParam("key"));

        // find out which host has the key
        KeyQueryMetadata metadata =
                kafkaStreams.queryMetadataForKey(LEADERBOARD_STORE_NAME, productId, Serdes.Long().serializer());

        // the local instance has this key
        if (hostInfo.equals(metadata.activeHost())) {
            System.out.println("Querying local store for key");
            Leaderboard highScores = getStore().get(productId);

            if (Objects.isNull(highScores)) {
                // game was not found
                ctx.status(404);
                return;
            }

            // game was found, so return the high scores
            ctx.json(highScores.toList());
            return;
        }

        // a remote instance has the key
        String remoteHost = metadata.activeHost().host();
        int remotePort = metadata.activeHost().port();
        String url =
                String.format(
                        "http://%s:%d/leaderboard/%s",
                        // params
                        remoteHost, remotePort, productId);

        // issue the request
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("Querying remote store for key");
            ctx.result(Objects.requireNonNull(response.body()).string());
        } catch (Exception e) {
            ctx.status(500);
        }
    }


}
