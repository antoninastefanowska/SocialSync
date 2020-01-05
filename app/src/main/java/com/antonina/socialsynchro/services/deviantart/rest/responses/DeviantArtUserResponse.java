package com.antonina.socialsynchro.services.deviantart.rest.responses;

import com.google.gson.annotations.SerializedName;

public class DeviantArtUserResponse extends DeviantArtResponse {
    @SerializedName("userid")
    private String userID;

    @SerializedName("username")
    private String username;

    @SerializedName("usericon")
    private String userIcon;

    @SerializedName("stats")
    private Stats stats;

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public int getWatcherCount() {
        return stats.getWatcherCount();
    }

    public static class Stats {
        @SerializedName("watchers")
        private int watcherCount;

        public int getWatcherCount() {
            return watcherCount;
        }
    }
}
