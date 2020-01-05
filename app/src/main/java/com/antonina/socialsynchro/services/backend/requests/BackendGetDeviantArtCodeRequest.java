package com.antonina.socialsynchro.services.backend.requests;

public class BackendGetDeviantArtCodeRequest extends BackendRequest {
    private final String state;

    private BackendGetDeviantArtCodeRequest(String authorizationString, String state) {
        super(authorizationString);
        this.state = state;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getState() {
        return state;
    }

    public static class Builder extends BackendRequest.Builder {
        private String state;

        @Override
        public BackendGetDeviantArtCodeRequest build() {
            configureAuthorization();
            return new BackendGetDeviantArtCodeRequest(authorization.buildAuthorizationString(), state);
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }
    }
}
