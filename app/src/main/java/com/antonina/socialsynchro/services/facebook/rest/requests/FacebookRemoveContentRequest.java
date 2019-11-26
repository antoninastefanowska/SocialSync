package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookUserAuthorizationStrategy;

public class FacebookRemoveContentRequest extends FacebookRequest {
    private final String postID;

    private FacebookRemoveContentRequest(String authorizationString, String postID) {
        super(authorizationString);
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
        private String accessToken;

        @Override
        public FacebookRemoveContentRequest build() {
            configureAuthorization();
            return new FacebookRemoveContentRequest(authorization.buildAuthorizationString(), postID);
        }

        public Builder postID(String postID) {
            this.postID = postID;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        private void configureAuthorization() {
            authorization = new FacebookUserAuthorizationStrategy(accessToken);
        }
    }
}
