package com.antonina.socialsynchro.services.twitter.requests;

public class TwitterRemoveContentRequest extends TwitterRequest {
    private final String id;

    private TwitterRemoveContentRequest(String authorizationHeader, String id) {
        super(authorizationHeader);
        this.id = id;
    }

    public String getID() { return id; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private String id;
        private String accessToken;
        private String secretToken;

        @Override
        public TwitterRemoveContentRequest build() {
            return new TwitterRemoveContentRequest(buildUserAuthorizationHeader(), id);
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder secretToken(String secretToken) {
            this.secretToken = secretToken;
            return this;
        }

        @Override
        protected String getURL() {
            return "https://api.twitter.com/1.1/statuses/destroy/" + id + ".json";
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
            authorizationParameters.put("oauth_token", getAccessToken());
            super.collectParameters();
        }
    }
}
