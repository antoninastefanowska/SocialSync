package com.antonina.socialsynchro.services.twitter.requests;

import com.antonina.socialsynchro.services.twitter.requests.authorization.TwitterUserAuthorizationStrategy;

public class TwitterGetAccessTokenRequest extends TwitterRequest {
    private final String verifier;

    private TwitterGetAccessTokenRequest(String authorizationHeader, String verifier) {
        super(authorizationHeader);
        this.verifier = verifier;
    }

    public String getVerifier() {
        return percentEncode(verifier);
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
            prepareAuthorization();
            return new TwitterGetAccessTokenRequest(authorization.buildAuthorizationHeader(), verifier);
        }

        @Override
        protected void prepareAuthorization() {
            authorization = new TwitterUserAuthorizationStrategy()
                    .accessToken(loginToken)
                    .secretToken(secretLoginToken)
                    .requestMethod("POST")
                    .requestURL(REQUEST_URL);
            authorization.addAuthorizationParameter("oauth_verifier", verifier);
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
