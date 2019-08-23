package com.antonina.socialsynchro.services.callback.requests;

import com.antonina.socialsynchro.services.IRequest;

public class CallbackGetVerifierRequest implements IRequest {
    private String loginToken;

    private CallbackGetVerifierRequest(String loginToken) {
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

        public CallbackGetVerifierRequest build() {
            return new CallbackGetVerifierRequest(loginToken);
        }

        public Builder loginToken(String loginToken) {
            this.loginToken = loginToken;
            return this;
        }
    }
}
