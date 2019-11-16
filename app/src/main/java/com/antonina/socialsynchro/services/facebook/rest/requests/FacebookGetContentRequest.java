package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookAuthorizationStrategy;

public class FacebookGetContentRequest extends FacebookRequest {
    private final String postID;

    private FacebookGetContentRequest(String authorizationHeader, String postID) {
        super(authorizationHeader);
        this.postID = postID;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPostID() {
        return postID;
    }

    public static class Builder extends FacebookRequest.Builder {
        private String postID;

        @Override
        public FacebookGetContentRequest build() {
            return new FacebookGetContentRequest(authorization.buildAuthorizationString(), postID);
        }

        public Builder postID(String postID) {
            this.postID = postID;
            return this;
        }

        public Builder authorizationStrategy(FacebookAuthorizationStrategy authorizationStrategy) {
            authorization = authorizationStrategy;
            return this;
        }
    }
}
