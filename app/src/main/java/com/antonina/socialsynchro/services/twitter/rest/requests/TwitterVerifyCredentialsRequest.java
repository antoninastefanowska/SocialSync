package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterUserAuthorizationStrategy;

public class TwitterVerifyCredentialsRequest extends TwitterRequest {
    private TwitterVerifyCredentialsRequest(String authorizationHeader) {
        super(authorizationHeader);
    }

    public static String getRequestEndpoint() {
        return "/account/verify_credentials";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
        private String accessToken;
        private String secretToken;

        @Override
        public TwitterVerifyCredentialsRequest build() {
            configureAuthorization();
            return new TwitterVerifyCredentialsRequest(authorization.buildAuthorizationHeader());
        }

        @Override
        protected void configureAuthorization() {
            authorization = new TwitterUserAuthorizationStrategy()
                    .accessToken(accessToken)
                    .secretToken(secretToken)
                    .requestMethod("GET")
                    .requestURL(REQUEST_URL);
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder secretToken(String secretToken) {
            this.secretToken = secretToken;
            return this;
        }
    }
}
