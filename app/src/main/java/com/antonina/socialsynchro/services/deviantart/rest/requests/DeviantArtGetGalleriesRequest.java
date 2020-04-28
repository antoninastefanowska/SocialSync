package com.antonina.socialsynchro.services.deviantart.rest.requests;

import com.antonina.socialsynchro.services.deviantart.rest.authorization.DeviantArtAuthorizationStrategy;

public class DeviantArtGetGalleriesRequest extends DeviantArtRequest {
    private final Integer offset;

    private DeviantArtGetGalleriesRequest(String authorizationString, Integer offset) {
        super(authorizationString);
        this.offset = offset;
    }

    public int getLimit() {
        return 50;
    }

    public Integer getOffset() {
        return offset;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DeviantArtRequest.Builder {
        private Integer offset;
        private String accessToken;

        @Override
        public DeviantArtGetGalleriesRequest build() {
            configureAuthorization();
            return new DeviantArtGetGalleriesRequest(authorization.buildAuthorizationString(), offset);
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        private void configureAuthorization() {
            authorization = new DeviantArtAuthorizationStrategy(accessToken);
        }
    }
}
