package com.antonina.socialsynchro.services.twitter.requests;

import com.antonina.socialsynchro.services.twitter.requests.authorization.TwitterUserAuthorizationStrategy;

public class TwitterCheckUploadStatusRequest extends TwitterRequest {
    private final String command;
    private final String mediaID;

    private TwitterCheckUploadStatusRequest(String authorizationHeader, String mediaID) {
        super(authorizationHeader);
        this.command = percentEncode("STATUS");
        this.mediaID = percentEncode(mediaID);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getMediaID() {
        return mediaID;
    }

    public String getCommand() {
        return command;
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://upload.twitter.com/1.1/media/upload.json";
        private String accessToken;
        private String secretToken;
        private String mediaID;

        @Override
        public TwitterCheckUploadStatusRequest build() {
            configureAuthorization();
            return new TwitterCheckUploadStatusRequest(authorization.buildAuthorizationHeader(), mediaID);
        }

        @Override
        protected void configureAuthorization() {
            authorization = new TwitterUserAuthorizationStrategy()
                    .accessToken(accessToken)
                    .secretToken(secretToken)
                    .requestMethod("GET")
                    .requestURL(REQUEST_URL)
                    .addSignatureParameter("command", "STATUS")
                    .addSignatureParameter("media_id", mediaID);
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
    }
}
