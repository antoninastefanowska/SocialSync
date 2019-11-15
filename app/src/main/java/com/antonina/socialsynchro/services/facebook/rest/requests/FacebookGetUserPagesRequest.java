package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookAuthorizationStrategy;

public class FacebookGetUserPagesRequest extends FacebookRequest {
    private final String userID;

    private FacebookGetUserPagesRequest(String authorizationHeader, String userID) {
        super(authorizationHeader);
        this.userID = userID;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUserID() {
        return userID;
    }

    public static class Builder extends FacebookRequest.Builder {
        private String userID;

        @Override
        public FacebookGetUserPagesRequest build() {
            return new FacebookGetUserPagesRequest(authorization.buildAuthorizationHeader(), userID);
        }

        public Builder userID(String userID) {
            this.userID = userID;
            return this;
        }

        public Builder authorizationStrategy(FacebookAuthorizationStrategy authorizationStrategy) {
            authorization = authorizationStrategy;
            return this;
        }
    }
}
