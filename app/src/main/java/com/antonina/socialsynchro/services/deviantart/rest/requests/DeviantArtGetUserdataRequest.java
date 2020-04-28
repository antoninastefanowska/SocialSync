package com.antonina.socialsynchro.services.deviantart.rest.requests;

import com.antonina.socialsynchro.services.deviantart.rest.authorization.DeviantArtAuthorizationStrategy;

public class DeviantArtGetUserdataRequest extends DeviantArtRequest {

    private DeviantArtGetUserdataRequest(String authorizationString) {
        super(authorizationString);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DeviantArtRequest.Builder {
        private String accessToken;

        @Override
        public DeviantArtGetUserdataRequest build() {
            configureAuthorization();
            return new DeviantArtGetUserdataRequest(authorization.buildAuthorizationString());
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        private void configureAuthorization() {
            authorization = new DeviantArtAuthorizationStrategy(accessToken);
        }
    }
}
