package com.antonina.socialsynchro.services.twitter.responses;

import com.antonina.socialsynchro.base.IResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TwitterTweetResponse implements IResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("extended_tweet")
    private ExtendedTweet extendedTweet; // TODO: Nie działa, być może do wyrzucenia.

    @SerializedName("errors")
    private ArrayList<Error> errors;

    public ExtendedTweet getExtendedTweet() { return extendedTweet; }

    public void setExtendedTweet(ExtendedTweet extendedTweet) { this.extendedTweet = extendedTweet; }

    public String getFullText() { return extendedTweet.getFullText(); }

    public String getID() { return id; }

    public String getErrors() {
        StringBuilder sb = new StringBuilder();

        for (Error error : errors) {
            sb.append(" code: ");
            sb.append(Integer.toString(error.getCode()));
            sb.append(" message: ");
            sb.append(error.getMessage());
        }

        return sb.toString();
    }

    public class ExtendedTweet {
        @SerializedName("full_text")
        private String fullText;

        public String getFullText() { return fullText; }

        public void setFullText(String fullText) { this.fullText = fullText; }
    }

    private class Error {
        @SerializedName("message")
        private String message;

        @SerializedName("code")
        private Integer code;

        public String getMessage() { return message; }

        public Integer getCode() { return code; }
    }
}
