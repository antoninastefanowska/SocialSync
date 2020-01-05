package com.antonina.socialsynchro.services.deviantart.rest.requests;

import com.antonina.socialsynchro.services.deviantart.rest.authorization.DeviantArtAuthorizationStrategy;

public class DeviantArtGetDeviationRequest extends DeviantArtRequest {
    private final String deviationID;

    private DeviantArtGetDeviationRequest(String authorizationString, String deviationID) {
        super(authorizationString);
        this.deviationID = deviationID;
    }

    public String getDeviationID() {
        return deviationID;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DeviantArtRequest.Builder {
        private String accessToken;
        private String deviationID;

        @Override
        public DeviantArtGetDeviationRequest build() {
            configureAuthorization();
            return new DeviantArtGetDeviationRequest(authorization.buildAuthorizationString(), deviationID);
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder deviationID(String deviationID) {
            this.deviationID = deviationID;
            return this;
        }

        private void configureAuthorization() {
            authorization = new DeviantArtAuthorizationStrategy(accessToken);
        }
    }
}
