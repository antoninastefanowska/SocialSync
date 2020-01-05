package com.antonina.socialsynchro.services.deviantart.rest.requests;

import com.antonina.socialsynchro.services.deviantart.rest.authorization.DeviantArtAuthorizationStrategy;

public class DeviantArtStashPublishRequest extends DeviantArtRequest {
    private final String stashID;

    private DeviantArtStashPublishRequest(String authorizationString, String stashID) {
        super(authorizationString);
        this.stashID = stashID;
    }

    public String getStashID() {
        return stashID;
    }

    public boolean isMature() {
        return false;
    }

    public boolean agreeSubmission() {
        return true;
    }

    public boolean agreeTOS() {
        return true;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DeviantArtRequest.Builder {
        private String accessToken;
        private String stashID;

        @Override
        public DeviantArtStashPublishRequest build() {
            configureAuthorization();
            return new DeviantArtStashPublishRequest(authorization.buildAuthorizationString(), stashID);
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder stashID(String stashID) {
            this.stashID = stashID;
            return this;
        }

        private void configureAuthorization() {
            authorization = new DeviantArtAuthorizationStrategy(accessToken);
        }
    }
}
