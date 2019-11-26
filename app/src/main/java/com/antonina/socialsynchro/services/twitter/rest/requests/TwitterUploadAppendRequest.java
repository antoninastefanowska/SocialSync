package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterUserAuthorizationStrategy;

import okhttp3.RequestBody;

public class TwitterUploadAppendRequest extends TwitterRequest {
    private final String command;
    private final String mediaID;
    private final String segmentIndex;
    private final RequestBody media;

    private TwitterUploadAppendRequest(String authorizationString, String mediaID, String segmentIndex, RequestBody media) {
        super(authorizationString);
        this.command = percentEncode("APPEND");
        this.mediaID = percentEncode(mediaID);
        this.segmentIndex = percentEncode(segmentIndex);
        this.media = media;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getMediaID() {
        return mediaID;
    }

    public String getSegmentIndex() {
        return segmentIndex;
    }

    public RequestBody getMedia() {
        return media;
    }

    public String getCommand() {
        return command;
    }

    public static String getRequestEndpoint() {
        return "/media/upload";
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://upload.twitter.com/1.1/media/upload.json";
        private String accessToken;
        private String secretToken;
        private String mediaID;
        private String segmentIndex;
        private RequestBody media;

        @Override
        public TwitterUploadAppendRequest build() {
            configureAuthorization();
            return new TwitterUploadAppendRequest(authorization.buildAuthorizationString(), mediaID, segmentIndex, media);
        }

        @Override
        protected void configureAuthorization() {
            authorization = new TwitterUserAuthorizationStrategy()
                    .accessToken(accessToken)
                    .secretToken(secretToken)
                    .requestMethod("POST")
                    .requestURL(REQUEST_URL)
                    .addSignatureParameter("command", "APPEND")
                    .addSignatureParameter("media_id", mediaID)
                    .addSignatureParameter("segment_index", segmentIndex);
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
    }
}
