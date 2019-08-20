package com.antonina.socialsynchro.services.twitter.requests;

public class TwitterGetLoginTokenRequest extends TwitterRequest {
    private TwitterGetLoginTokenRequest(String authorizationHeader) {
        super(authorizationHeader);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String CALLBACK_URL = "https://socialsynchro.pythonanywhere.com/callback/post_verifier";

        @Override
        public TwitterGetLoginTokenRequest build() {
            return new TwitterGetLoginTokenRequest(buildUserAuthorizationHeader());
        }

        @Override
        protected String getURL() {
            return "https://api.twitter.com/oauth/request_token";
        }

        @Override
        protected String getAccessToken() {
            return "";
        }

        @Override
        protected String getSecretToken() {
            return "";
        }

        @Override
        protected String getHTTPMethod() {
            return "POST";
        }

        @Override
        protected void collectParameters() {
            authorizationParameters.put("oauth_callback", CALLBACK_URL);
            super.collectParameters();
        }
    }
}
