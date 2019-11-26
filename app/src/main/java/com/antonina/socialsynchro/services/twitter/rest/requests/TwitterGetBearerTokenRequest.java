package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterGetBearerTokenAuthorizationStrategy;

public class TwitterGetBearerTokenRequest extends TwitterRequest {
    private final String grantType;

    private TwitterGetBearerTokenRequest(String authorizationString) {
        super(authorizationString);
        this.grantType = percentEncode("client_credentials");
    }

    public String getGrantType() {
        return grantType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {

        @Override
        public TwitterGetBearerTokenRequest build() {
            configureAuthorization();
            return new TwitterGetBearerTokenRequest(authorization.buildAuthorizationString());
        }

        @Override
        protected void configureAuthorization() {
            authorization = new TwitterGetBearerTokenAuthorizationStrategy();
        }
    }
}
