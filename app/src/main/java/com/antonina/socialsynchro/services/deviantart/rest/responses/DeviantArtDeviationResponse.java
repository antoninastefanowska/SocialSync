package com.antonina.socialsynchro.services.deviantart.rest.responses;

import com.google.gson.annotations.SerializedName;

public class DeviantArtDeviationResponse extends DeviantArtResponse {
    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("stats")
    private Stats stats;

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return url;
    }

    public int getFavouriteCount() {
        return stats.getFavouriteCount();
    }

    public int getCommentCount() {
        return stats.getCommentCount();
    }

    public static class Stats {
        @SerializedName("comments")
        private int commentCount;

        @SerializedName("favourites")
        private int favouriteCount;

        public int getCommentCount() {
            return commentCount;
        }

        public int getFavouriteCount() {
            return favouriteCount;
        }
    }
}
