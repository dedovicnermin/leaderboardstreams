package io.nermdev.kafka.leaderboardstreams.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class IgnoreSchemaPropertyMixinConfig {
    @JsonIgnore
    abstract void getClassSchema();

    @JsonIgnore
    abstract void getSpecificData();

    @JsonIgnore
    abstract void get();

    @JsonIgnore
    abstract void getSchema();
}
