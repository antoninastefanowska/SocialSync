package com.antonina.socialsynchro.services.twitter.responses;

import com.google.gson.annotations.SerializedName;

public class TwitterContentResponse extends TwitterResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("extended_tweet")
    private ExtendedTweet extendedTweet; // TODO: Nie działa, być może do wyrzucenia.

    public ExtendedTweet getExtendedTweet() { return extendedTweet; }

    public String getFullText() { return extendedTweet.getFullText(); }

    public String getID() { return id; }

    public static class ExtendedTweet {
        @SerializedName("full_text")
        private String fullText;

        public String getFullText() { return fullText; }
    }
}