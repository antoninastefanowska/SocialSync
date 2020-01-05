package com.antonina.socialsynchro.services.deviantart.rest.requests;

import com.antonina.socialsynchro.services.deviantart.rest.authorization.DeviantArtAuthorizationStrategy;

public class DeviantArtWhoAmIRequest extends DeviantArtRequest {
    private DeviantArtWhoAmIRequest(String authorizationString) {
        super(authorizationString);
    }

    public String getExpandOptions() {
        return "user.stats";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DeviantArtRequest.Builder {
        private String accessToken;

        @Override
        public DeviantArtWhoAmIRequest build() {
            configureAuthorization();
            return new DeviantArtWhoAmIRequest(authorization.buildAuthorizationString());
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
