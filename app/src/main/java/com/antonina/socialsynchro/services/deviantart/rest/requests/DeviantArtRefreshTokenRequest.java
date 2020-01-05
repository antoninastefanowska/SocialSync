package com.antonina.socialsynchro.services.deviantart.rest.requests;

public class DeviantArtRefreshTokenRequest extends DeviantArtRequest {
    private final String clientID;
    private final String clientSecret;
    private final String refreshToken;

    private DeviantArtRefreshTokenRequest(String authorizationString, String clientID, String clientSecret, String refreshToken) {
        super(authorizationString);
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.refreshToken = refreshToken;
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getGrantType() {
        return "refresh_token";
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DeviantArtRequest.Builder {
        private String clientID;
        private String clientSecret;
        private String refreshToken;

        @Override
        public DeviantArtRefreshTokenRequest build() {
            return new DeviantArtRefreshTokenRequest(null, clientID, clientSecret, refreshToken);
        }

        public Builder clientID(String clientID) {
            this.clientID = clientID;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }
    }
}
