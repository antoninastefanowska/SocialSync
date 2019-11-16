package com.antonina.socialsynchro.services.backend.requests;

public class BackendGetFacebookTokenRequest extends BackendRequest {
    private final String state;

    private BackendGetFacebookTokenRequest(String state, String authorizationHeader) {
        super(authorizationHeader);
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
        public BackendGetFacebookTokenRequest build() {
            configureAuthorization();
            return new BackendGetFacebookTokenRequest(state, authorization.buildAuthorizationString());
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }
    }
}
