package com.antonina.socialsynchro.services.twitter.requests;

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
        private String loginToken;
        private String secretLoginToken;
        private String verifier;

        @Override
        public TwitterGetAccessTokenRequest build() {
            return new TwitterGetAccessTokenRequest(buildUserAuthorizationHeader(), verifier);
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

        @Override
        protected String getURL() {
            return "https://api.twitter.com/oauth/access_token";
        }

        @Override
        protected String getAccessToken() {
            return loginToken;
        }

        @Override
        protected String getSecretToken() {
            return secretLoginToken;
        }

        @Override
        protected String getHTTPMethod() { return "POST"; }

        @Override
        protected void collectParameters() {
            authorizationParameters.put("oauth_token", getAccessToken());
            authorizationParameters.put("oauth_verifier", verifier);
            super.collectParameters();
        }
    }
}
