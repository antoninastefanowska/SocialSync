package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookUserAuthorizationStrategy;

public class FacebookCreateContentRequest extends FacebookRequest {
    private final String pageID;
    private final String message;

    protected FacebookCreateContentRequest(String authorizationString, String pageID, String message) {
        super(authorizationString);
        this.pageID = pageID;
        this.message = message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPageID() {
        return pageID;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder extends FacebookRequest.Builder {
        protected String pageID;
        protected String message;
        protected String accessToken;

        @Override
        public FacebookCreateContentRequest build() {
            configureAuthorization();
            return new FacebookCreateContentRequest(authorization.buildAuthorizationString(), pageID, message);
        }

        public Builder pageID(String pageID) {
            this.pageID = pageID;
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

        protected void configureAuthorization() {
            authorization = new FacebookUserAuthorizationStrategy(accessToken);
        }
    }
}
