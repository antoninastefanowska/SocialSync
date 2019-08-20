package com.antonina.socialsynchro.services.twitter.requests;

public class TwitterCreateContentRequest extends TwitterRequest {
    private String status;

    private TwitterCreateContentRequest(String authorizationHeader, String status) {
        super(authorizationHeader);
        this.status = status;
    }

    public String getStatus() {
        return percentEncode(status);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private String status;
        private String accessToken;
        private String secretToken;

        @Override
        public TwitterCreateContentRequest build() {
            return new TwitterCreateContentRequest(buildUserAuthorizationHeader(), status);
        }

        public Builder status(String status) {
            this.status = status;
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
            return "https://api.twitter.com/1.1/statuses/update.json";
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
            authorizationParameters.put("status", status);
            authorizationParameters.put("oauth_token", getAccessToken());
            super.collectParameters();
            authorizationParameters.remove("status");
        }
    }
}
