package com.antonina.socialsynchro.services.twitter.requests;

public class TwitterUploadFinalizeRequest extends TwitterRequest {
    private String mediaID;

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
        private String accessToken;
        private String secretToken;
        private String mediaID;

        @Override
        public TwitterUploadFinalizeRequest build() {
            return new TwitterUploadFinalizeRequest(buildUserAuthorizationHeader(), mediaID);
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
            authorizationParameters.put("command", "FINALIZE");
            authorizationParameters.put("media_id", mediaID);
            authorizationParameters.put("oauth_token", getAccessToken());
            super.collectParameters();
            authorizationParameters.remove("command");
            authorizationParameters.remove("media_id");
        }
    }
}
