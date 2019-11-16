package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookUserAuthorizationStrategy;

public class FacebookUpdateContentRequest extends FacebookRequest {
    private final String postID;
    private final String message;

    private FacebookUpdateContentRequest(String authorizationHeader, String postID, String message) {
        super(authorizationHeader);
        this.postID = postID;
        this.message = message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPostID() {
        return postID;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder extends FacebookRequest.Builder {
        private String postID;
        private String message;
        private String accessToken;

        @Override
        public FacebookUpdateContentRequest build() {
            configureAuthorization();
            return new FacebookUpdateContentRequest(authorization.buildAuthorizationString(), postID, message);
        }

        public Builder postID(String postID) {
            this.postID = postID;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
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
