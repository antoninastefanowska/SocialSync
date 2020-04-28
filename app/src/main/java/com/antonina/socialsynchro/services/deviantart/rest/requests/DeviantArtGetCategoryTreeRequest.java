package com.antonina.socialsynchro.services.deviantart.rest.requests;

import com.antonina.socialsynchro.services.deviantart.rest.authorization.DeviantArtAuthorizationStrategy;

public class DeviantArtGetCategoryTreeRequest extends DeviantArtRequest {
    private final String catpath;

    private DeviantArtGetCategoryTreeRequest(String authorizationString, String catpath) {
        super(authorizationString);
        this.catpath = catpath;
    }

    public String getCatpath() {
        return catpath;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DeviantArtRequest.Builder {
        private String accessToken;
        private String catpath;

        @Override
        public DeviantArtGetCategoryTreeRequest build() {
            configureAuthorization();
            return new DeviantArtGetCategoryTreeRequest(authorization.buildAuthorizationString(), catpath);
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder catpath(String catpath) {
            this.catpath = catpath;
            return this;
        }

        private void configureAuthorization() {
            authorization = new DeviantArtAuthorizationStrategy(accessToken);
        }
    }
}
