package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterAuthorizationStrategy;
import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterUserAuthorizationStrategy;

public class TwitterGetUserRequest extends TwitterRequest {
    private final String userID;

    protected TwitterGetUserRequest(String authorizationString, String userID) {
        super(authorizationString);
        this.userID = percentEncode(userID);
    }

    public String getUserID() {
        return userID;
    }

    public static String getRequestEndpoint() {
        return "/users/show/:id";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://api.twitter.com/1.1/users/show.json";
        private String userID;

        @Override
        public TwitterGetUserRequest build() {
            configureAuthorization();
            return new TwitterGetUserRequest(authorization.buildAuthorizationString(), userID);
        }

        @Override
        protected void configureAuthorization() {
            if (authorization.isUserAuthorization()) {
                ((TwitterUserAuthorizationStrategy)authorization)
                        .requestMethod("GET")
                        .requestURL(REQUEST_URL)
                        .addSignatureParameter("user_id", userID);
            }
        }

        public Builder userID(String userID) {
            this.userID = userID;
            return this;
        }

        public Builder authorizationStrategy(TwitterAuthorizationStrategy authorizationStrategy) {
            authorization = authorizationStrategy;
            return this;
        }
    }
}
