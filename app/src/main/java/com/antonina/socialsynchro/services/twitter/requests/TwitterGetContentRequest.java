package com.antonina.socialsynchro.services.twitter.requests;

import com.antonina.socialsynchro.services.twitter.requests.authorization.TwitterAuthorizationStrategy;
import com.antonina.socialsynchro.services.twitter.requests.authorization.TwitterUserAuthorizationStrategy;

public class TwitterGetContentRequest extends TwitterRequest {
    private final String id;

    private TwitterGetContentRequest(String authorizationHeader, String id) {
        super(authorizationHeader);
        this.id = percentEncode(id);
    }

    public String getID() {
        return id;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://api.twitter.com/1.1/statuses/show.json";
        private String id;

        @Override
        public TwitterGetContentRequest build() {
            configureAuthorization();
            return new TwitterGetContentRequest(authorization.buildAuthorizationHeader(), id);
        }

        @Override
        protected void configureAuthorization() {
            if (authorization.isUserAuthorization()) {
                ((TwitterUserAuthorizationStrategy)authorization)
                        .requestMethod("GET")
                        .requestURL(REQUEST_URL)
                        .addSignatureParameter("id", id);
            }
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder authorizationStrategy(TwitterAuthorizationStrategy authorizationStrategy) {
            authorization = authorizationStrategy;
            return this;
        }
    }
}
