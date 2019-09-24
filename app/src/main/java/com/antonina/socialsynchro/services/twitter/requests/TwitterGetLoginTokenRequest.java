package com.antonina.socialsynchro.services.twitter.requests;

import com.antonina.socialsynchro.services.twitter.requests.authorization.TwitterUserAuthorizationStrategy;

public class TwitterGetLoginTokenRequest extends TwitterRequest {
    private TwitterGetLoginTokenRequest(String authorizationHeader) {
        super(authorizationHeader);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
        private final static String CALLBACK_URL = "https://socialsynchro.pythonanywhere.com/callback/post_verifier";

        @Override
        public TwitterGetLoginTokenRequest build() {
            configureAuthorization();
            return new TwitterGetLoginTokenRequest(authorization.buildAuthorizationHeader());
        }

        @Override
        protected void configureAuthorization() {
            authorization = new TwitterUserAuthorizationStrategy()
                    .secretToken("")
                    .requestMethod("POST")
                    .requestURL(REQUEST_URL)
                    .addAuthorizationParameter("oauth_callback", CALLBACK_URL);
        }
    }
}
