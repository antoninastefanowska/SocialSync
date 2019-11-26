package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterUserAuthorizationStrategy;

public class TwitterGetAccessTokenRequest extends TwitterRequest {
    private final String verifier;

    private TwitterGetAccessTokenRequest(String authorizationString, String verifier) {
        super(authorizationString);
        this.verifier = percentEncode(verifier);
    }

    public String getVerifier() {
        return verifier;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://api.twitter.com/oauth/access_token";
        private String loginToken;
        private String secretLoginToken;
        private String verifier;

        @Override
        public TwitterGetAccessTokenRequest build() {
            configureAuthorization();
            return new TwitterGetAccessTokenRequest(authorization.buildAuthorizationString(), verifier);
        }

        @Override
        protected void configureAuthorization() {
            authorization = new TwitterUserAuthorizationStrategy()
                    .accessToken(loginToken)
                    .secretToken(secretLoginToken)
                    .requestMethod("POST")
                    .requestURL(REQUEST_URL)
                    .addAuthorizationParameter("oauth_verifier", verifier);
        }

        public Builder loginToken(String loginToken) {
            this.loginToken = loginToken;
            return this;
        }

        public Builder secretLoginToken(String secretLoginToken) {
            this.secretLoginToken = secretLoginToken;
            return this;
        }

        public Builder verifier(String verifier) {
            this.verifier = verifier;
            return this;
        }
    }
}
