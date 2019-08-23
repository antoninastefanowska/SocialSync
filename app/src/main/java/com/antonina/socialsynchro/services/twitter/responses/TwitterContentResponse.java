package com.antonina.socialsynchro.services.twitter.responses;

import com.antonina.socialsynchro.services.ErrorResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TwitterContentResponse extends TwitterResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("extended_tweet")
    private ExtendedTweet extendedTweet; // TODO: Nie działa, być może do wyrzucenia.

    @SerializedName("errors")
    private ArrayList<ErrorResponse> errors;

    public ExtendedTweet getExtendedTweet() { return extendedTweet; }

    public void setExtendedTweet(ExtendedTweet extendedTweet) { this.extendedTweet = extendedTweet; }

    public String getFullText() { return extendedTweet.getFullText(); }

    public String getID() { return id; }

    @Override
    public String getErrorString() {
        if (errors == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (ErrorResponse error : errors) {
            sb.append(error.toString());
            sb.append('\n');
        }
        return sb.toString();
    }

    public class ExtendedTweet {
        @SerializedName("full_text")
        private String fullText;

        public String getFullText() { return fullText; }

        public void setFullText(String fullText) { this.fullText = fullText; }
    }
}