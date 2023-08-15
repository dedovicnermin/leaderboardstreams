package io.nermdev.kafka.leaderboardstreams;

public enum LeaderboardType {
    PLAYER(LeaderboardService.LEADERBOARDS_STATE_STORE_TOP_10_SCORES_GLOBAL,"/players/%s"),
    PLAYER_HISTORIC(LeaderboardService.LEADERBOARDS_STATE_STORE_TOP_10_HISTORIC_SCORES_GLOBAL,"/historic/players/%s"),
    PRODUCT(LeaderboardService.LEADERBOARDS_STATE_STORE_TOP_10_SCORES_PRODUCT,"/products/%s"),
    PRODUCT_HISTORIC(LeaderboardService.LEADERBOARDS_STATE_STORE_TOP_10_HISTORIC_SCORES_PRODUCT,"/historic/products/%s");

    private final String stateStoreName;
    private final String urlSuffix;
    LeaderboardType(String stateStoreName, String urlSuffix) {
        this.stateStoreName = stateStoreName;
        this.urlSuffix = urlSuffix;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public String getStateStoreName() {
        return stateStoreName;
    }
}
