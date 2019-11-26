package com.antonina.socialsynchro.services.twitter.rest.responses;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("WeakerAccess")
public class TwitterContentResponse extends TwitterResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("extended_tweet")
    private ExtendedTweet extendedTweet;
    public ExtendedTweet getExtendedTweet() { return extendedTweet; }

    @SerializedName("retweet_count")
    private int retweetCount;

    @SerializedName("favorite_count")
    private int favoriteCount;

    public String getFullText() { return extendedTweet.getFullText(); }

    public String getID() { return id; }

    public int getRetweetCount() {
        return retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public static class ExtendedTweet {
        @SerializedName("full_text")
        private String fullText;

        public String getFullText() { return fullText; }
    }
}