package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterUserAuthorizationStrategy;

public class TwitterRemoveContentRequest extends TwitterRequest {
    private final String id;

    private TwitterRemoveContentRequest(String authorizationHeader, String id) {
        super(authorizationHeader);
        this.id = id;
    }

    public String getID() { return id; }

    public static String getRequestEndpoint() {
        return "/statuses/destroy/:id";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://api.twitter.com/1.1/statuses/destroy/";
        private String id;
        private String accessToken;
        private String secretToken;

        @Override
        public TwitterRemoveContentRequest build() {
            configureAuthorization();
            return new TwitterRemoveContentRequest(authorization.buildAuthorizationHeader(), id);
        }

        @Override
        protected void configureAuthorization() {
            authorization = new TwitterUserAuthorizationStrategy()
                    .accessToken(accessToken)
                    .secretToken(secretToken)
                    .requestMethod("POST")
                    .requestURL(REQUEST_URL + id + ".json");
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
    }
}
