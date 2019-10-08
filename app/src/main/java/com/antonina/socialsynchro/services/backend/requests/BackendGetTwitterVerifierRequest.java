package com.antonina.socialsynchro.services.backend.requests;

public class BackendGetTwitterVerifierRequest extends BackendRequest {
    private final String loginToken;

    private BackendGetTwitterVerifierRequest(String authorizationHeader, String loginToken) {
        super(authorizationHeader);
        this.loginToken = loginToken;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BackendRequest.Builder {
        private String loginToken;

        @Override
        public BackendGetTwitterVerifierRequest build() {
            configureAuthorization();
            return new BackendGetTwitterVerifierRequest(authorization.buildAuthorizationHeader(), loginToken);
        }

        public Builder loginToken(String loginToken) {
            this.loginToken = loginToken;
            return this;
        }
    }
}
