package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookApplicationAuthorizationStrategy;
import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookAuthorizationStrategy;

public class FacebookInspectTokenRequest extends FacebookRequest {
    private final String inputToken;

    private FacebookInspectTokenRequest(String authorizationHeader, String inputToken) {
        super(authorizationHeader);
        this.inputToken = inputToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getInputToken() {
        return inputToken;
    }

    public static class Builder extends FacebookRequest.Builder {
        private String inputToken;

        @Override
        public FacebookInspectTokenRequest build() {
            configureAuthorization();
            return new FacebookInspectTokenRequest(authorization.buildAuthorizationHeader(), inputToken);
        }

        private void configureAuthorization() {
            authorization = new FacebookApplicationAuthorizationStrategy();
        }

        public Builder inputToken(String inputToken) {
            this.inputToken = inputToken;
            return this;
        }
    }
}
