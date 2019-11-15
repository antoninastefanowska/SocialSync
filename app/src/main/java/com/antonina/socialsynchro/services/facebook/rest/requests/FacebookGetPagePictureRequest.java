package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookAuthorizationStrategy;

public class FacebookGetPagePictureRequest extends FacebookRequest {
    private final String pageID;
    private final boolean redirect;

    private FacebookGetPagePictureRequest(String authorizationHeader, String pageID) {
        super(authorizationHeader);
        this.pageID = pageID;
        redirect = false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPageID() {
        return pageID;
    }

    public boolean getRedirect() {
        return redirect;
    }

    public static class Builder extends FacebookRequest.Builder {
        private String pageID;

        @Override
        public FacebookGetPagePictureRequest build() {
            return new FacebookGetPagePictureRequest(authorization.buildAuthorizationHeader(), pageID);
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
