package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookAuthorizationStrategy;

public class FacebookGetPageRequest extends FacebookRequest {
    private final String pageID;

    private FacebookGetPageRequest(String authorizationHeader, String pageID) {
        super(authorizationHeader);
        this.pageID = pageID;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPageID() {
        return pageID;
    }

    public static class Builder extends FacebookRequest.Builder {
        private String pageID;

        @Override
        public FacebookGetPageRequest build() {
            return new FacebookGetPageRequest(authorization.buildAuthorizationHeader(), pageID);
        }

        public Builder pageID(String pageID) {
            this.pageID = pageID;
            return this;
        }

        public Builder authorizationStrategy(FacebookAuthorizationStrategy authorizationStrategy) {
            authorization = authorizationStrategy;
            return this;
        }
    }
}
