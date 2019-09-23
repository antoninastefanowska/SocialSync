package com.antonina.socialsynchro.services.twitter.requests;

import okhttp3.RequestBody;

public class TwitterUploadAppendRequest extends TwitterRequest {
    private final String mediaID;
    private final String segmentIndex;
    private final RequestBody media;

    private TwitterUploadAppendRequest(String authorizationHeader, String mediaID, String segmentIndex, RequestBody media) {
        super(authorizationHeader);
        this.mediaID = mediaID;
        this.segmentIndex = segmentIndex;
        this.media = media;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getMediaID() {
        return percentEncode(mediaID);
    }

    public String getSegmentIndex() {
        return percentEncode(segmentIndex);
    }

    public RequestBody getMedia() {
        return media;
    }

    public String getCommand() {
        return percentEncode("APPEND");
    }

    public static class Builder extends TwitterRequest.Builder {
        private String accessToken;
        private String secretToken;
        private String mediaID;
        private String segmentIndex;
        private RequestBody media;

        @Override
        public TwitterUploadAppendRequest build() {
            return new TwitterUploadAppendRequest(buildUserAuthorizationHeader(), mediaID, segmentIndex, media);
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder secretToken(String secretToken) {
            this.secretToken = secretToken;
            return this;
        }

        public Builder mediaID(String mediaID) {
            this.mediaID = mediaID;
            return this;
        }

        public Builder segmentIndex(int segmentIndex) {
            this.segmentIndex = String.valueOf(segmentIndex);
            return this;
        }

        public Builder media(RequestBody media) {
            this.media = media;
            return this;
        }

        @Override
        protected String getURL() {
            return "https://upload.twitter.com/1.1/media/upload.json";
        }

        @Override
        protected String getAccessToken() {
            return accessToken;
        }

        @Override
        protected String getSecretToken() {
            return secretToken;
        }

        @Override
        protected String getHTTPMethod() {
            return "POST";
        }

        @Override
        protected void collectParameters() {
            authorizationParameters.put("command", "APPEND");
            authorizationParameters.put("media_id", mediaID);
            authorizationParameters.put("segment_index", segmentIndex);
            authorizationParameters.put("oauth_token", getAccessToken());
            super.collectParameters();
            authorizationParameters.remove("command");
            authorizationParameters.remove("media_id");
            authorizationParameters.remove("segment_index");
        }
    }
}
