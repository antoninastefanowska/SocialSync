package com.antonina.socialsynchro.services.twitter.requests;

public class TwitterVerifyCredentialsRequest extends TwitterRequest {
    private TwitterVerifyCredentialsRequest(String authorizationHeader) {
        super(authorizationHeader);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private String accessToken;
        private String secretToken;

        @Override
        public TwitterVerifyCredentialsRequest build() {
            return new TwitterVerifyCredentialsRequest(buildUserAuthorizationHeader());
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
            return "https://api.twitter.com/1.1/account/verify_credentials.json";
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
            return "GET";
        }

        @Override
        protected void collectParameters() {
            authorizationParameters.put("oauth_token", getAccessToken());
            super.collectParameters();
        }
    }
}
