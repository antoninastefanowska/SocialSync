package com.antonina.socialsynchro.services.backend.requests;

import com.antonina.socialsynchro.common.rest.IRequest;

public class BackendGetTwitterVerifierRequest implements IRequest {
    private final String loginToken;

    private BackendGetTwitterVerifierRequest(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String loginToken;

        public BackendGetTwitterVerifierRequest build() {
            return new BackendGetTwitterVerifierRequest(loginToken);
        }

        public Builder loginToken(String loginToken) {
            this.loginToken = loginToken;
            return this;
        }
    }
}
