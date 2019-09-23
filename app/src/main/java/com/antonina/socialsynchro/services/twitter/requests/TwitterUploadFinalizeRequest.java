package com.antonina.socialsynchro.services.twitter.requests;

import com.antonina.socialsynchro.services.twitter.requests.authorization.TwitterUserAuthorizationStrategy;

public class TwitterUploadFinalizeRequest extends TwitterRequest {
    private final String mediaID;

    private TwitterUploadFinalizeRequest(String authorizationHeader, String mediaID) {
        super(authorizationHeader);
        this.mediaID = mediaID;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getMediaID() {
        return percentEncode(mediaID);
    }

    public String getCommand() {
        return percentEncode("FINALIZE");
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://upload.twitter.com/1.1/media/upload.json";
        private String accessToken;
        private String secretToken;
        private String mediaID;

        @Override
        public TwitterUploadFinalizeRequest build() {
            prepareAuthorization();
            return new TwitterUploadFinalizeRequest(authorization.buildAuthorizationHeader(), mediaID);
        }

        @Override
        protected void prepareAuthorization() {
            authorization = new TwitterUserAuthorizationStrategy()
                    .accessToken(accessToken)
                    .secretToken(secretToken)
                    .requestMethod("POST")
                    .requestURL(REQUEST_URL);
            authorization.addSignatureParameter("command", "FINALIZE");
            authorization.addSignatureParameter("media_id", mediaID);
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
