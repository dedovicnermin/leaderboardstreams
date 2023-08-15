package io.nermdev.kafka.leaderboardstreams;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.nermdev.kafka.leaderboardstreams.serde.JsonDeserializer;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import io.nermdev.kafka.leaderboardstreams.serde.JsonSerializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class StreamUtils {
    private StreamUtils(){}

    public static final String PROPERTIES_FILE_PATH = "src/main/resources/streams.properties";

    public static void loadConfig(final String file, final Properties properties) {
        try (
                final FileInputStream inputStream = new FileInputStream(file)
        ) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Map<String,Object> propertiesToMap(final Properties properties) {
        final Map<String, Object> configs = new HashMap<>();
        properties.forEach(
                (key, value) -> configs.put((String) key, value)
        );
        return configs;
    }

    public static <T> Serde<T> getJsonSerde(Class<T> tClass) {
        final JsonSerializer<T> serializer = new JsonSerializer<>();
        final JsonDeserializer<T> deserializer = new JsonDeserializer<>(tClass);
        return Serdes.serdeFrom(serializer,deserializer);
    }

    public static <T extends SpecificRecord> SpecificAvroSerde<T> getAvroSerde(final Map<String, Object> serdeConfig) {
        final SpecificAvroSerde<T> avroSerde = new SpecificAvroSerde<>();
        avroSerde.configure(serdeConfig, false);
        return avroSerde;
    }
}
