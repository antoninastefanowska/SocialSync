package com.antonina.socialsynchro.services.deviantart.rest.requests;

import com.antonina.socialsynchro.services.deviantart.rest.DeviantArtClient;

public class DeviantArtGetAccessTokenRequest extends DeviantArtRequest {
    private final String clientID;
    private final String clientSecret;
    private final String code;

    private DeviantArtGetAccessTokenRequest(String clientID, String clientSecret, String code, String authorizationString) {
        super(authorizationString);
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.code = code;
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getGrantType() {
        return "authorization_code";
    }

    public String getCode() {
        return code;
    }

    public String getRedirectURL() {
        return DeviantArtClient.getCallbackURL();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DeviantArtRequest.Builder {
        private String clientID;
        private String clientSecret;
        private String code;

        @Override
        public DeviantArtGetAccessTokenRequest build() {
            return new DeviantArtGetAccessTokenRequest(clientID, clientSecret, code, null);
        }

        public Builder clientID(String clientID) {
            this.clientID = clientID;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }
    }
}
